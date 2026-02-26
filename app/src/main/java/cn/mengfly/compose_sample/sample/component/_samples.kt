package cn.mengfly.compose_sample.sample.component

import cn.mengfly.compose_sample.Heading
import cn.mengfly.compose_sample.Sample

val basicComponentSampleList = arrayOf(
    Heading("基础组件"),
    Sample(
        title = "Scaffold",
        description = "Scaffold 页面脚手架",
        content = { ScaffoldSample() },
        articleUrl = "https://mp.weixin.qq.com/s/aSbLgn3Ln6tPxsdXEFB0fw",
        source = listOf("ScaffoldSample.kt")
    ),

)