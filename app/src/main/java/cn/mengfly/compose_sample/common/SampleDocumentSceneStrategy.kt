package cn.mengfly.compose_sample.common

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import cn.mengfly.compose_sample.common.pages.CommonPage

class SampleDocumentSceneStrategy<T : NavKey> : SceneStrategy<T> {
    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        val sampleEntry = entries.firstOrNull { it.metadata[SAMPLE_KEY] == true }
        val documentEntry = entries.firstOrNull { it.metadata[DOCUMENT_KEY] == true }

        return if (sampleEntry != null && documentEntry != null) {
            SampleDocumentScene(sampleEntry, documentEntry, entries.dropLast(2))
        } else {
            null
        }

    }

    companion object {
        const val SAMPLE_KEY = "sample"
        const val DOCUMENT_KEY = "document"

        fun sample(): Map<String, Any> = mapOf(SAMPLE_KEY to true)
        fun document(): Map<String, Any> = mapOf(DOCUMENT_KEY to true)
    }
}

class SampleDocumentScene<T : NavKey>(
    val sampleEntry: NavEntry<T>,
    val documentEntry: NavEntry<T>,
    override val previousEntries: List<NavEntry<T>>,
) : Scene<T> {

    override val key: Any = sampleEntry.contentKey
    override val entries: List<NavEntry<T>> = listOf(sampleEntry, documentEntry)

    override val content: @Composable (() -> Unit) = {
        ShowDocumentView(
            sample = { sampleEntry.Content() },
            document = { documentEntry.Content() }
        )
    }
}

@Composable
fun ShowDocumentView(sample: @Composable () -> Unit,
                     document: @Composable () -> Unit
) {

    Box(Modifier.fillMaxSize()) {

        // 先显示样例
        sample()

        var documentVisible by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
                .widthIn(max = 500.dp)
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    documentVisible = !documentVisible
                },
                colors = IconButtonDefaults.iconButtonColors().copy(
                    containerColor = Color(0x66AAAAAA)
                ),
                shape = RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp),
                modifier = Modifier.size(width = 18.dp, height = 64.dp)
            ) {
                Icon(
                    imageVector = if (documentVisible) Icons.Default.ChevronRight else Icons.Default.ChevronLeft,
                    contentDescription = "openDocument"
                )
            }
            if (documentVisible) {
                document()
            }
        }
    }

}

@PreviewScreenSizes
@Composable
fun ShowDocumentViewPreview() {
    ShowDocumentView(
        sample = { CommonPage("sample", Color.Red) },
        document = { CommonPage("document", Color.Blue) }
    )
}