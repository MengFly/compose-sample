package cn.mengfly.compose_sample.sample.navigation3

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.scene.OverlayScene
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import androidx.navigation3.ui.NavDisplay
import cn.mengfly.compose_sample.common.pages.DishDetail
import cn.mengfly.compose_sample.common.pages.DishDetailPage
import cn.mengfly.compose_sample.common.pages.DishList
import cn.mengfly.compose_sample.common.pages.DishListPage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation3SceneBottomSheetSample() {
    val backStack = rememberNavBackStack(DishList)
    val strategy = remember { BottomSheetSceneStrategy<NavKey>() }
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
                metadata = BottomSheetSceneStrategy.bottomSheet(ModalBottomSheetProperties())
            ) { detail ->
                DishDetailPage(detail.id)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
private class BottomSheetScene<T : Any>(

    override val key: T,
    override val previousEntries: List<NavEntry<T>>,
    override val overlaidEntries: List<NavEntry<T>>,
    private val entry: NavEntry<T>,
    private val modalBottomSheetProperties: ModalBottomSheetProperties,
    private val onBack: () -> Unit

) : OverlayScene<T> {

    override val entries: List<NavEntry<T>> = listOf(entry)

    override val content: @Composable (() -> Unit) = {
        ModalBottomSheet(
            onDismissRequest = onBack,
            properties = modalBottomSheetProperties
        ) {
            entry.Content()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as BottomSheetScene<*>

        return key == other.key &&
                previousEntries == other.previousEntries &&
                overlaidEntries == other.overlaidEntries &&
                entry == other.entry &&
                modalBottomSheetProperties == other.modalBottomSheetProperties
    }

    override fun hashCode(): Int {
        return key.hashCode() * 31 +
                previousEntries.hashCode() * 31 +
                overlaidEntries.hashCode() * 31 +
                entry.hashCode() * 31 +
                modalBottomSheetProperties.hashCode() * 31
    }

}

private class BottomSheetSceneStrategy<T : Any>() : SceneStrategy<T> {


    @OptIn(ExperimentalMaterial3Api::class)
    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        val lastEntry = entries.lastOrNull()

        val bottomSheetProperties =
            lastEntry?.metadata?.get(BOTTOM_SHEET_KEY) as? ModalBottomSheetProperties
        return bottomSheetProperties?.let {
            @Suppress("UNCHECKED_CAST")
            BottomSheetScene(
                key = lastEntry.contentKey as T,
                previousEntries = entries.dropLast(1),
                overlaidEntries = entries.dropLast(1),
                entry = lastEntry,
                modalBottomSheetProperties = it,
                onBack = {
                    onBack()
                }
            )
        }
    }

    companion object {
        const val BOTTOM_SHEET_KEY = "bottomSheet"

        @OptIn(ExperimentalMaterial3Api::class)
        fun bottomSheet(properties: ModalBottomSheetProperties): Map<String, Any> {
            return mapOf(BOTTOM_SHEET_KEY to properties)
        }
    }
}