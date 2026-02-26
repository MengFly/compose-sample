package cn.mengfly.compose_sample.sample.navigation3

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.core.net.toUri
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor

private const val DEEPLINK_PREFIX = "https://demo.compose.mengfly.cn"

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class DeepLink(
    val pattern: String
)

@Serializable
@DeepLink("${DEEPLINK_PREFIX}/home")
private data object DeepLinkHome : NavKey

@Serializable
@DeepLink("${DEEPLINK_PREFIX}/detail/{id}")
private data class DeepLinkDishDetail(val id: Int) : NavKey

//private val deeplinkResolver = DeeplinkResolver().apply {
//    register(Home.serializer())
//    register(DishDetail.serializer())
//}

/**
 * 深度链接样例
 */
@Composable
fun Navigation3DeepLinkSample() {


}


private class DeeplinkResolver(
) {
    private val deepLinks = mutableListOf<DeeplinkElement<*>>()

    inline fun <reified T : NavKey> register(serializer: KSerializer<T>) {
        val deepLinkAnno = T::class.annotations.firstOrNull { it is DeepLink }
        if (deepLinkAnno != null) {
            val pattern = (deepLinkAnno as DeepLink).pattern
            deepLinks.add(
                DeeplinkElement(pattern, serializer)
            )
        }

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
    private val patternUri by lazy { pattern.toUri() }

    private val elementDescriptorMap by lazy {
        val descriptorMap = mutableMapOf<String, SerialDescriptor>()
        for (i in 0 until serializer.descriptor.elementsCount) {
            val descriptor = serializer.descriptor.getElementDescriptor(i)
            descriptorMap[descriptor.serialName] = descriptor
        }
        return@lazy descriptorMap
    }

    fun matcher(uri: Uri): T? {
        if (uri.scheme != patternUri.scheme) {
            return null
        }
        if (uri.host != patternUri.host) {
            return null
        }
        if (uri.pathSegments.size != patternUri.pathSegments.size) {
            return null
        }
        // 开始匹配
        val params = mutableMapOf<String, Any>()
        for (i in uri.pathSegments.indices) {
            val pathSegment = uri.pathSegments[i]
            val patternSegment = patternUri.pathSegments[i]
            if (patternSegment.startsWith("{") && patternSegment.endsWith("}")) {
                val key = patternSegment.substring(1, patternSegment.length - 1)
                // 无效的参数
                val descriptor = elementDescriptorMap[key] ?: return null
//                params[key] = descriptor.kind
            } else {
                if (pathSegment != patternSegment) {
                    return null
                }
            }
        }

        return null
    }
}
