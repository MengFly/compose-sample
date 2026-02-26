package cn.mengfly.compose_sample.sample.navigation3

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import cn.mengfly.compose_sample.common.DishManager
import cn.mengfly.compose_sample.common.component.DropDownMenu
import cn.mengfly.compose_sample.common.pages.DishDetail
import cn.mengfly.compose_sample.common.pages.DishDetailPage
import cn.mengfly.compose_sample.common.pages.DishList
import cn.mengfly.compose_sample.common.pages.DishListPage
import cn.mengfly.compose_sample.common.pages.PictureDetail
import cn.mengfly.compose_sample.common.pages.PictureDetailPage

private const val DEEPLINK_PREFIX = "https://demo.compose.mengfly.cn"

// 注册深度链接
private val deeplinkResolver = DeeplinkResolver().apply {
    register(DishList.serializer(), "${DEEPLINK_PREFIX}/home")
    register(DishDetail.serializer(), "${DEEPLINK_PREFIX}/dishDetail/{id}")
}

/**
 * 深度链接样例
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun Navigation3DeepLinkSample() {

    val startActivityLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { }

    Scaffold(Modifier.fillMaxSize()) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "深度链接样例",
                style = MaterialTheme.typography.titleLarge
            )

            var pageTypeIndex by remember { mutableIntStateOf(0) }
            var dishId by remember { mutableIntStateOf(0) }
            val pageType = listOf("菜单列表页面", "菜品详情页面")

            var deeplink by remember { mutableStateOf("$DEEPLINK_PREFIX/home") }

            DropDownMenu(
                label = "选择要进入的页面类型",
                options = pageType,
                selectedIndex = pageTypeIndex
            ) {
                pageTypeIndex = it
                deeplink = if (pageTypeIndex == 0) {
                    "${DEEPLINK_PREFIX}/home"
                } else {
                    "${DEEPLINK_PREFIX}/dishDetail/${dishId+1}"
                }
            }

            if (pageTypeIndex == 1) {

                DropDownMenu(
                    label = "选择要进入的菜品编号",
                    options = DishManager.dishes.map { it.name },
                    selectedIndex = dishId
                ) {
                    dishId = it
                    deeplink = "${DEEPLINK_PREFIX}/dishDetail/${dishId+1}"
                }
            }


            Text("深度链接地址：")
            Text(
                text = deeplink,
                Modifier.selectable(true, onClick = {})
            )

            TextButton(onClick = {
                startActivityLauncher.launch(Intent().apply {
                    data = deeplink.toUri()
                    // 启动一个新栈
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
            }) {
                Text(text = "进入深度链接Activity")
            }

            Text("点击后选择 Compose-Sample App")


            Text(
                text = "深度链接本质上就是将连接地址(通常是url)通过Intent的方式传递给Activity," +
                        "在启动Activity后通过url解析出对应的路由。\n" +
                        "之后使用Navigation3根据这个路由显示对应的页面，完成从url到页面跳转。\n" +
                        "这里的重点并不是Navigation3, 重点是怎么从url解析出路由。\n" +
                        "最后路由跳转究竟是自己写跳转，还是用Navigation2或者Navigation3都可以。",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = Color.Gray,
                ),
                lineHeight = MaterialTheme.typography.labelLarge.lineHeight,
                modifier = Modifier.padding(16.dp)
            )
        }
    }

}


class DeepLinkActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 匹配深度链接，如果匹配不到，则使用 默认路由
        val key = deeplinkResolver.resolve(intent.data.toString()) ?: DishList
        enableEdgeToEdge()

        /*
         * 下方的实现方式不变，唯一变化的就是获取默认路径变了
         *
         * 如果是通过深度链接跳转的，通过深度链接解析器解析到深度链接对应的路由，则使用这个路由
         *
         * 如果不是深度链接跳转，或者匹配不到深度链接，则使用默认路由
         */
        setContent {

            val navBackStack = rememberNavBackStack(key)

            NavDisplay(
                backStack = navBackStack,
                entryProvider = entryProvider {
                    // 主页
                    entry<DishList> {
                        DishListPage(onDishSelect = {
                            navBackStack.removeIf { nk -> nk is DishDetail }
                            navBackStack.add(DishDetail(it))
                        })
                    }
                    // 详情页
                    entry<DishDetail> { detail ->
                        DishDetailPage(detail.id) {
                            navBackStack.removeIf { nk -> nk is PictureDetail }
                            navBackStack.add(PictureDetail(it))
                        }
                    }
                    // 图片详情页
                    entry<PictureDetail> {
                        PictureDetailPage(picture = it.picture)
                    }

                },
                // 页面入栈动画
                transitionSpec = {
                    slideInHorizontally(
                        // 从画面右侧滑入(初始位置在右侧)
                        initialOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(1000)
                    ) togetherWith slideOutHorizontally(
                        // 从画面左侧滑出（滑动到 -fullWidth（左侧））
                        targetOffsetX = { fullWidth -> -fullWidth },
                        animationSpec = tween(1000)
                    )
                },
                // 页面出栈动画
                popTransitionSpec = {
                    slideInHorizontally(
                        // 从屏幕左侧滑入（初始位置在左侧（-fullWidth））
                        initialOffsetX = { fullWidth -> -fullWidth },
                        animationSpec = tween(1000)
                    ) togetherWith slideOutHorizontally(
                        // 从屏幕右侧滑出（滑动到 fullWidth（右侧））
                        targetOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(1000)
                    )
                },
                predictivePopTransitionSpec = {
                    slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(1000)
                    ) togetherWith slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(1000)
                    )
                }
            )
        }
    }
}

