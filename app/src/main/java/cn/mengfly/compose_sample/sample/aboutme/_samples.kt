package cn.mengfly.compose_sample.sample.aboutme

import cn.mengfly.compose_sample.Heading
import cn.mengfly.compose_sample.Sample

val aboutMeSampleList = arrayOf(
    Heading("About"),
    Sample(
        title = "我的公众号",
        description = "关注公众号，获取更多技术文章",
        content = { MyWechat() }
    )
)