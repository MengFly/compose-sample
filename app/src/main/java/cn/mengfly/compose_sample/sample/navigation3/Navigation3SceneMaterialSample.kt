package cn.mengfly.compose_sample.sample.navigation3

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import cn.mengfly.compose_sample.common.pages.DishDetail
import cn.mengfly.compose_sample.common.pages.DishDetailPage
import cn.mengfly.compose_sample.common.pages.DishList
import cn.mengfly.compose_sample.common.pages.DishListPage
import cn.mengfly.compose_sample.common.pages.PictureDetail
import cn.mengfly.compose_sample.common.pages.PictureDetailPage

/**
 * Navigation3 为我们提供了开箱即用的场景策略，包含在 androidx.compose.material3.adaptive:adaptive-navigation 包中
 *
 * 不过目前该包仍处于 alpha 版本
 *
 * 该包提供了以下场景策略：
 * 1. ListDetailSceneStrategy
 * 2. SupportingPaneSceneStrategy
 */

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun Navigation3SceneMaterialSample() {
    val backStack = rememberNavBackStack(DishList)
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    // 需要引入依赖：androidx.compose.material3.adaptive:adaptive-layout
    val directive = remember(windowAdaptiveInfo) {
        calculatePaneScaffoldDirective(windowAdaptiveInfo)
            .copy(horizontalPartitionSpacerSize = 0.dp)
    }
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>(directive = directive)

    NavDisplay(
        backStack = backStack,
        sceneStrategy = listDetailStrategy,
        entryProvider = entryProvider {
            entry<DishList>(
                metadata = ListDetailSceneStrategy.listPane(
                    detailPlaceholder = {
                        Text(
                            "请选择一个菜品", modifier = Modifier.fillMaxSize(),
                            textAlign = TextAlign.Center
                        )

                    }
                )
            ) {
                DishListPage {
                    backStack.removeIf { nk -> nk is DishDetail }
                    backStack.add(DishDetail(it))
                }
            }
            entry<DishDetail>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) {
                DishDetailPage(it.id) { id ->
                    backStack.removeIf { nk -> nk is PictureDetail }
                    backStack.add(PictureDetail(id))
                }
            }

            entry<PictureDetail>(
                metadata = ListDetailSceneStrategy.extraPane()
            ) {
                PictureDetailPage(it.picture)
            }
        }

    )
}