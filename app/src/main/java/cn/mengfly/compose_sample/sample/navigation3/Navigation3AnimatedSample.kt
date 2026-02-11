package cn.mengfly.compose_sample.sample.navigation3

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import androidx.navigation3.ui.NavDisplay
import cn.mengfly.compose_sample.common.pages.DishDetail
import cn.mengfly.compose_sample.common.pages.DishDetailPage
import cn.mengfly.compose_sample.common.pages.DishList
import cn.mengfly.compose_sample.common.pages.DishListPage
import cn.mengfly.compose_sample.common.pages.PictureDetail
import cn.mengfly.compose_sample.common.pages.PictureDetailPage


@Composable
fun Navigation3AnimatedSample() {

    // 创建导航栈，通过该导航栈进行页面跳转控制
    val navBackStack = rememberNavBackStack(DishList)

    NavDisplay(
        // 将页面返回栈传递给 NavDisplay 组件
        backStack = navBackStack,
        // 这里我们实现了一个无任何跳转动画策略的场景策略
        // 从这里就可以验证出来，动画是作用于 Scene 的
        // 一旦我们强制给Scene设置了动画，那么无论是 entry设置的，还是NavDisplay设置的，都会不起作用
//        sceneStrategy = NoAnimationSceneStrategy(),
        entryProvider = entryProvider {

            // 主页
            entry<DishList> {
                DishListPage(onDishSelect = {
                    navBackStack.removeIf { nk -> nk is DishDetail }
                    navBackStack.add(DishDetail(it))
                })
            }
            // 详情页
            entry<DishDetail>(
                metadata = NavDisplay.transitionSpec {
                    slideInVertically(
                        // 从下方滑入
                        initialOffsetY = { it },
                        animationSpec = tween(1000)
                        // 退出动画(无动画，保证主页面不动）
                    ) togetherWith ExitTransition.KeepUntilTransitionsFinished
                } + NavDisplay.popTransitionSpec {
                    // 退出动画（无动画）
                    EnterTransition.None togetherWith
                            // 向下方划出
                            slideOutVertically(
                                targetOffsetY = { it },
                                animationSpec = tween(1000)
                            )
                } + NavDisplay.predictivePopTransitionSpec {
                    EnterTransition.None togetherWith
                            // 向下方划出
                            slideOutVertically(
                                targetOffsetY = { it },
                                animationSpec = tween(1000)
                            )
                }
            ) { detail ->
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

/**
 * 无动画场景策略
 */
private class NoAnimationSceneStrategy<T : Any> : SceneStrategy<T> {
    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T> {
        return NoAnimationScene(
            entry = entries.last(),
            previousEntries = entries.dropLast(1)
        )
    }
}

private class NoAnimationScene<T : Any>(
    val entry: NavEntry<T>,
    override val previousEntries: List<NavEntry<T>>
) : Scene<T> {
    override val key: Any = entry.contentKey
    override val entries: List<NavEntry<T>> = listOf(entry)
    override val content: @Composable (() -> Unit) = {
        entry.Content()
    }

    /**
     * 通过元数据固定设置为无动画
     */
    override val metadata: Map<String, Any>
        get() =
            NavDisplay.transitionSpec { EnterTransition.None togetherWith ExitTransition.None } +
                    NavDisplay.popTransitionSpec { EnterTransition.None togetherWith ExitTransition.None } +
                    NavDisplay.predictivePopTransitionSpec { EnterTransition.None togetherWith ExitTransition.None }
}
