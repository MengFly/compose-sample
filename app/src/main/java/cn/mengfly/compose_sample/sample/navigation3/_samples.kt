package cn.mengfly.compose_sample.sample.navigation3

import cn.mengfly.compose_sample.Heading
import cn.mengfly.compose_sample.Sample

val navigation3Samples = arrayOf(
    Heading("Navigation3"),
    Sample(
        title = "Navigation3-Basic",
        description = "Navigation3基础使用方式（如何组合多个页面，如何传递参数，如何控制页面跳转）",
        content = { Navigation3BasicSample() },
        source = listOf("Navigation3BasicSample.kt"),
        articleUrl = "https://mp.weixin.qq.com/s/93o4Y5146kBedukOhdL30Q"
    ),
    Sample(
        title = "Navigation3-Scene(Dialog)",
        description = "Scene使用方式（弹窗案例）",
        content = { Navigation3SceneDialogSample() },
        source = listOf("Navigation3SceneDialogSample.kt")
    ),
    Sample(
        title = "Navigation3-Scene(BottomSheet)",
        description = "Scene使用方式（底部弹窗案例）",
        content = { Navigation3SceneBottomSheetSample() },
        source = listOf("Navigation3SceneBottomSheetSample.kt")
    ),
    Sample(
        title = "Navigation3-Scene(ListDetail)",
        description = "Scene使用方式（列表与详情页面案例，推荐使用平板模式）",
        content = { Navigation3SceneListDetailSample() },
        source = listOf("Navigation3SceneListDetailSample.kt")
    ),
    Sample(
        title = "Navigation3-Scene(Adaptive ListDetail)",
        description = "Scene使用方式（Adaptive Material3 自带适配场景）",
        content = { Navigation3SceneMaterialSample() },
        source = listOf("Navigation3SceneMaterialSample.kt")
    ),
    Sample(
        title = "Navigation3-Scene(Adaptive Supporting)",
        description = "Scene使用方式（Adaptive Material3 自带适配场景）",
        content = { Navigation3SceneMaterialSupportingSample() },
        source = listOf("Navigation3SceneMaterialSupportingSample.kt")
    ),
    Sample(
        title = "Navigation3-Animated",
        description = "Navigation3动画使用方式",
        content = { Navigation3AnimatedSample() },
        source = listOf("Navigation3AnimatedSample.kt")
    ),
    Sample(
        title = "Navigation3-DeepLink",
        description = "Navigation3深度链接",
        content = { Navigation3DeepLinkSample() },
        source = listOf("Navigation3DeepLinkSample.kt", "DeeplinkResolver.kt")
    ),
    Sample(
        title = "Navigation3-AutoDeepLink",
        description = "Navigation3自动深度链接",
        content = { Navigation3AutoDeepLinkSample() },
        source = listOf("Navigation3AutoDeepLinkSample.kt")
    ),
)