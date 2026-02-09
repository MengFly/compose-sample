package cn.mengfly.compose_sample.sample.navigation3

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import cn.mengfly.compose_sample.common.pages.DishDetail
import cn.mengfly.compose_sample.common.pages.DishDetailPage
import cn.mengfly.compose_sample.common.pages.DishList
import cn.mengfly.compose_sample.common.pages.DishListPage
import cn.mengfly.compose_sample.common.pages.Welcome
import cn.mengfly.compose_sample.common.pages.WelcomePage
import cn.mengfly.compose_sample.ui.theme.ComposeSampleTheme


@Composable
fun Navigation3BasicSample() {

    ComposeSampleTheme {
        // 创建导航栈，通过该导航栈进行页面跳转控制
        val navBackStack = rememberNavBackStack(Welcome)

        NavDisplay(
            // 将页面返回栈传递给 NavDisplay 组件
            backStack = navBackStack,
            modifier = Modifier.fillMaxSize(),
            // 我们希望监听返回事件，使用 onBack 来监听
            // 其实默认的逻辑和下面的逻辑一致，这里写出来只是为了介绍
            onBack = {
                // 在返回时，移除栈顶的页面，也就是显示上一个页面
                navBackStack.removeLastOrNull()
            },
            entryProvider = entryProvider {
                // 欢迎页面
                entry<Welcome> {
                    WelcomePage {
                        // 先移除掉欢迎页面，因为跳转后，欢迎页面将不再显示，不能点了返回之后又跳回到欢迎页面了
                        navBackStack.removeIf { it is Welcome }
                        // 将主页面添加到导航栈中
                        navBackStack.add(DishList)
                    }
                }
                // 主页
                entry<DishList> {
                    DishListPage(onDishSelect = {
                        // 点击菜品后，将菜品详情页面添加到导航栈中，显示该菜品详情页面
                        navBackStack.add(DishDetail(it))
                    })
                }
                // 菜品详情页
                entry<DishDetail> { detail ->
                    DishDetailPage(detail.id)
                }

            }
        )
    }
}