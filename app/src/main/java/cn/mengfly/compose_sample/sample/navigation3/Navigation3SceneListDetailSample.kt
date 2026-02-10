package cn.mengfly.compose_sample.sample.navigation3

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import androidx.navigation3.ui.NavDisplay
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import cn.mengfly.compose_sample.common.pages.DishDetail
import cn.mengfly.compose_sample.common.pages.DishDetailPage
import cn.mengfly.compose_sample.common.pages.DishList
import cn.mengfly.compose_sample.common.pages.DishListPage

@Composable
fun Navigation3SceneListDetailSample() {
    val backStack = rememberNavBackStack(DishList)
    val strategy = rememberListDetailSceneStrategy<NavKey>()
    NavDisplay(
        backStack = backStack,
        sceneStrategy = strategy,
        entryProvider = entryProvider {
            entry<DishList>(
                // 标记为列表页面
                metadata = ListDetailScene.listPane()
            ) {
                DishListPage(
                    onDishSelect = {
                        // 先移除所有详情页面
                        backStack.removeIf { navKey -> navKey is DishDetail }
                        backStack.add(DishDetail(it))
                    }
                )
            }
            entry<DishDetail>(
                // 标记为详情页面
                metadata = ListDetailScene.detailPane()
            ) { detail ->
                DishDetailPage(detail.id)
            }
        }
    )
}

@Composable
private fun <T : Any> rememberListDetailSceneStrategy(): SceneStrategy<T> {
    // 需要引入依赖：androidx.compose.material3.adaptive
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    return remember { ListDetailSceneStrategy(windowSizeClass) }
}

class ListDetailScene<T : Any>(
    override val key: T,
    override val previousEntries: List<NavEntry<T>>,
    val listEntry: NavEntry<T>,
    val detailEntry: NavEntry<T>?,
) : Scene<T> {

    /**
     * 包含的页面列表，列表页面和详情页面
     * 如果详情页面为null，则说明当前场景为列表页面
     */
    override val entries: List<NavEntry<T>>
        get() = if (detailEntry == null) {
            listOf(listEntry)
        } else {
            listOf(listEntry, detailEntry)
        }

    /**
     * 列表页面和详情页面的组合
     */
    override val content: @Composable (() -> Unit) = {

        val weightAnimate by animateFloatAsState(
            if (detailEntry == null) 1f else 0.4f
        )

        // 横向布局，列表页面占0.4，详情页面占0.6
        Row(modifier = Modifier.fillMaxSize()) {
            // 显示列表内容
            Column(modifier = Modifier.weight(weight = weightAnimate)) {
                listEntry.Content()
            }

            // 显示详情内容
            detailEntry?.let {
                Column(modifier = Modifier.weight(0.6f)) {
                    AnimatedContent(
                        targetState = detailEntry,
                        contentKey = { it.contentKey },
                        // content切换动画
                        transitionSpec = {
                            slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
                        }
                    ) {
                        it.Content()
                    }
                }
            }
        }
    }

    companion object {
        // 列表页面标记
        internal const val LIST_KEY = "ListDetailScene-List"
        // 详情页面标记
        internal const val DETAIL_KEY = "ListDetailScene-Detail"

        fun listPane() = mapOf(LIST_KEY to true)

        fun detailPane() = mapOf(DETAIL_KEY to true)
    }
}

/**
 * WindowSizeClass : 需要引入依赖： androidx.window:window-core
 */
class ListDetailSceneStrategy<T : Any>(val windowSizeClass: WindowSizeClass) : SceneStrategy<T> {

    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        // 判断是否是中等或大屏
        if (!windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND)) {
            return null
        }
        // 获取到列表的NavKey
        val listEntry =
            entries.lastOrNull { it.metadata[ListDetailScene.LIST_KEY] == true } ?: return null

        // 获取到详情的NavKey
        val detailEntry =
            entries.lastOrNull { it.metadata[ListDetailScene.DETAIL_KEY] == true }

        // 这里使用列表的Key作为场景的key
        // 原因是，如果场景Key变化的话，整个Scene页面就会重新创建，这样的话列表页面也会重新执行动画，此时可能就会出现列表页面一闪而过的情况
        // 由于列表页面是固定的，所以这里使用列表的Key作为场景的key，这样只会重新创建详情页面，动画只存在于详情页面
        val key = listEntry.contentKey

        // 不要这样设置
//        val key = detailEntry ?.contentKey ?: listEntry.contentKey
        @Suppress("UNCHECKED_CAST")
        return ListDetailScene(
            key = key as T,
            previousEntries = entries.dropLast(1),
            listEntry = listEntry,
            detailEntry = detailEntry
        )
    }
}