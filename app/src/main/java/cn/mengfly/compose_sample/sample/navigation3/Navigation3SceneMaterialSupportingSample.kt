package cn.mengfly.compose_sample.sample.navigation3

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior.Companion.PopUntilCurrentDestinationChange
import androidx.compose.material3.adaptive.navigation3.SupportingPaneSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberSupportingPaneSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun Navigation3SceneMaterialSupportingSample() {
    val backStack = rememberNavBackStack(DishList)
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()

    // 需要引入依赖：androidx.compose.material3.adaptive:adaptive-layout
    val directive = remember(windowAdaptiveInfo) {
        calculatePaneScaffoldDirective(windowAdaptiveInfo)
            .copy(
                horizontalPartitionSpacerSize = 0.dp
            )
    }
    val supportingPaneStrategy = rememberSupportingPaneSceneStrategy<NavKey>(
        directive = directive,
        backNavigationBehavior = PopUntilCurrentDestinationChange
    )

    NavDisplay(
        backStack = backStack,
        sceneStrategy = supportingPaneStrategy,
        entryProvider = entryProvider
        {
            entry<DishList>(
                metadata = SupportingPaneSceneStrategy.mainPane()
            ) {
                DishListPage {
                    backStack.removeIf { nk -> nk is DishDetail }
                    backStack.add(DishDetail(it))
                }
            }
            entry<DishDetail>(
                metadata = SupportingPaneSceneStrategy.supportingPane()
            ) {
                DishDetailPage(it.id) { id ->
                    backStack.removeIf { nk -> nk is PictureDetail }
                    backStack.add(PictureDetail(id))
                }
            }

            entry<PictureDetail>(
                metadata = SupportingPaneSceneStrategy.extraPane()
            ) {
                PictureDetailPage(it.picture)
            }
        }

    )
}