package cn.mengfly.compose_sample.common

/**
 * 获取Sample的相对包名
 */
fun getPackage(): String {
    Thread.currentThread().stackTrace.forEach { stackTraceElement ->
        val prefix = "cn.mengfly.compose_sample.sample."
        if (stackTraceElement.className.startsWith(prefix)) {
            return stackTraceElement.className
                .replace(prefix, "")
                .replace("._samplesKt", "")
                .replace(".", "/")
        }
    }
    return ""
}