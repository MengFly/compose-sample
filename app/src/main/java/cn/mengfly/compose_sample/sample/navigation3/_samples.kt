package cn.mengfly.compose_sample.sample.navigation3

import cn.mengfly.compose_sample.Heading
import cn.mengfly.compose_sample.Sample

val navigation3Samples = arrayOf(
    Heading("Navigation3"),
    Sample(
        title = "Navigation3-Basic",
        description = "Navigation3基础使用方式（如何组合多个页面，如何传递参数，如何控制页面跳转）",
        content = { Navigation3BasicSample() }
    ),
    Sample(
        title = "Navigation3-Scene(Dialog)",
        description = "Scene使用方式（弹窗案例）",
        content = { Navigation3SceneDialogSample() }
    ),
    Sample(
        title = "Navigation3-Scene(BottomSheet)",
        description = "Scene使用方式（底部弹窗案例）",
        content = { Navigation3SceneBottomSheetSample() }
    ),
    Sample(
        title = "Navigation3-Scene(ListDetail)",
        description = "Scene使用方式（列表与详情页面案例，推荐使用平板模式）",
        content = { Navigation3SceneListDetailSample() }
    ),
)