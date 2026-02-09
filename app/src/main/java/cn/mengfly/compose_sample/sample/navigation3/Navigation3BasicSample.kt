package cn.mengfly.compose_sample.sample.navigation3

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import cn.mengfly.compose_sample.common.pages.DishDetailPage
import cn.mengfly.compose_sample.common.pages.DishListPage
import cn.mengfly.compose_sample.common.pages.WelcomePage
import cn.mengfly.compose_sample.ui.theme.ComposeSampleTheme
import kotlinx.serialization.Serializable

/**
 * 欢迎页面Key
 */
@Serializable
data object Welcome : NavKey

/**
 * 主页Key
 */
@Serializable
data object Home : NavKey

/**
 * 菜品详情
 */
@Serializable
data class Detail(val id: Int) : NavKey

@Composable
fun Navigation3BasicSample() {

    ComposeSampleTheme {
        // 创建导航栈，通过该导航栈进行页面跳转控制
        val navBackStack = rememberNavBackStack(Welcome)

        NavDisplay(
            backStack = navBackStack,
            modifier = Modifier.fillMaxSize(),
            entryProvider = entryProvider {
                // 欢迎页面
                entry<Welcome> {
                    WelcomePage {
                        // 先移除掉欢迎页面，因为跳转后，欢迎页面将不再显示，不能点了返回之后又跳回到欢迎页面了
                        navBackStack.removeIf { it is Welcome }
                        // 将主页面添加到导航栈中
                        navBackStack.add(Home)
                    }
                }
                // 主页
                entry<Home> {
                    DishListPage(onDishSelect = {
                        navBackStack.add(Detail(it))
                    })
                }
                // 菜品详情页
                entry<Detail> { detail ->
                    DishDetailPage(detail.id)
                }

            }
        )
    }
}