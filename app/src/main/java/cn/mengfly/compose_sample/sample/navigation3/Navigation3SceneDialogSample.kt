package cn.mengfly.compose_sample.sample.navigation3

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import cn.mengfly.compose_sample.common.pages.DishDetailPage
import cn.mengfly.compose_sample.common.pages.DishList
import cn.mengfly.compose_sample.common.pages.DishListPage
import kotlinx.serialization.Serializable


@Serializable
private data class DishDetail(val id: Int) : NavKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation3SceneDialogSample() {
    val backStack = rememberNavBackStack(DishList)
    val strategy = remember { DialogSceneStrategy<NavKey>() }

    NavDisplay(
        modifier = Modifier.fillMaxWidth(),
        backStack = backStack,
        sceneStrategy = strategy,
        entryProvider = entryProvider {
            entry<DishList> {
                DishListPage(
                    onDishSelect = {
                        backStack.add(DishDetail(it))
                    }
                )
            }
            entry<DishDetail>(
                metadata = DialogSceneStrategy.dialog(DialogProperties(dismissOnBackPress = true))
            ) { detail ->
                DishDetailPage(detail.id)
            }
        }
    )
}
