package cn.mengfly.compose_sample.sample.navigation3

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.EmptySerializersModule


class DeeplinkResolver(
) {
    private val deepLinks = mutableListOf<DeeplinkElement<*>>()

    fun <T : NavKey> register(serializer: KSerializer<T>, pattern: String) {
        deepLinks.add(
            DeeplinkElement(pattern, serializer)
        )
    }

    fun resolve(data: String): NavKey? {
        val uri = data.toUri()

        for (element in deepLinks) {
            val key = element.matcher(uri)
            if (key != null) {
                return key
            }
        }
        return null
    }
}

@OptIn(ExperimentalSerializationApi::class)
private data class DeeplinkElement<T : NavKey>(
    val pattern: String,
    val serializer: KSerializer<T>
) {
    private val patternUri = pattern.toUri()

    private val pathSegments by lazy {
        buildList {
            // 获取uri中路径信息，并判断每一个子路径是否是参数
            patternUri.pathSegments.forEach {
                // 判断是否是参数
                if (it.startsWith("{") && it.endsWith("}")) {
                    add(PathSegment(it.substring(1, it.length - 1), true))
                } else {
                    add(PathSegment(it, false))
                }
            }
        }
    }

    fun matcher(uri: Uri): T? {

        // 从pathSegments中校验匹配，并且如果遇到路径是参数的情况，则将参数值保存起来
        val params = mutableMapOf<String, String>()
        if (matchAndPreparePathParameter(uri, params).not()) {
            // 如果路径匹配不成功，则返回null
            return null
        }

        // 从查询参数中获取参数信息
        prepareQueryParameter(uri, params)

        return try {
            MapToBeanDecoder(params).decodeSerializableValue(serializer)
        } catch (_: Exception) {
            null
        }
    }

    private fun prepareQueryParameter(uri: Uri,
                                      params: MutableMap<String, String>
    ) {

        // 如果携带了查询参数，将查询参数也保存到参数中
        uri.queryParameterNames.forEach {
            // 判断参数是否已经被添加到参数map中，为避免干扰，优先以路径参数为准
            if (!params.containsKey(it)) {
                params[it] = uri.getQueryParameter(it)!!
            }
        }
    }

    /**
     * 校验并且从路径中获取参数表
     * @return 如果路径完全匹配，则返回true， 否则返回false
     */
    private fun matchAndPreparePathParameter(uri: Uri,
                                             params: MutableMap<String, String>
    ): Boolean {
        // scheme、host、pathSegments个数必须严格匹配
        if (uri.scheme != patternUri.scheme ||
            uri.host != patternUri.host ||
            uri.pathSegments.size != patternUri.pathSegments.size) {
            return false
        }

        uri.pathSegments.asSequence().zip(pathSegments.asSequence()).forEach {
            // 如果是参数的情况，则将参数值保存起来
            if (it.second.isParamArg) {
                params[it.second.key] = it.first
            } else {
                // 非参数的情况下，路径需要严格匹配
                if (it.first != it.second.key) {
                    return false
                }
            }
        }
        return true
    }
}

private data class PathSegment(val key: String, val isParamArg: Boolean)

@OptIn(ExperimentalSerializationApi::class)
private class MapToBeanDecoder(
    private val map: Map<String, String>
) : AbstractDecoder() {
    override val serializersModule = EmptySerializersModule()

    /**
     * 当前正在处理的元素索引
     */
    private var elementIndex: Int = -1

    /**
     * 当前正在处理的元素名称
     */
    private var elementName: String = ""

    private var elementValue: Any? = null


    /**
     * 获取下一个要解析的元素的索引
     */
    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        // 从上一个已经处理完成的索引中获取下一个索引
        var currentIndex = elementIndex
        while (true) {
            currentIndex++

            // 如果当前索引超出了元素个数， 返回 DECODE_DONE
            if (currentIndex >= descriptor.elementsCount) {
                return CompositeDecoder.DECODE_DONE
            }

            val currentName = descriptor.getElementName(currentIndex)

            // 如果参数表中能找到这个索引,返回这个索引，并记录索引位置以及参数名称
            if (map.containsKey(currentName)) {
                elementIndex = currentIndex
                elementName = currentName
                // 解析值
                elementValue = try {
                    decodeElementValue(descriptor.getElementDescriptor(currentIndex), map[currentName])
                } catch (e: Exception) {
                    Log.e("MapToBeanDecoder", "decodeElementValue error: $currentName", e)
                    null
                }
                return currentIndex
            }
        }
    }

    private fun decodeElementValue(descriptor: SerialDescriptor, stringValue: String?): Any? {
        // 如果原本的值是null，则返回null
        if (stringValue == null) {
            return null
        }
        return when (descriptor.kind) {
            PrimitiveKind.STRING -> stringValue
            PrimitiveKind.INT -> stringValue.toInt()
            PrimitiveKind.BOOLEAN -> stringValue.toBoolean()
            PrimitiveKind.BYTE -> stringValue.toByte()
            PrimitiveKind.CHAR -> stringValue.toCharArray()
            PrimitiveKind.DOUBLE -> stringValue.toDouble()
            PrimitiveKind.FLOAT -> stringValue.toFloat()
            PrimitiveKind.LONG -> stringValue.toLong()
            PrimitiveKind.SHORT -> stringValue.toShort()
            else -> throw IllegalArgumentException("不支持的参数类型")

        }
    }

    override fun decodeValue(): Any {
        return elementValue!!
    }


    /**
     * 判断当前索引对应的元素是否不为null
     */
    override fun decodeNotNullMark(): Boolean = elementValue != null

}