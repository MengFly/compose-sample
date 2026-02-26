package cn.mengfly.compose_sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import cn.mengfly.compose_sample.common.SampleDocumentSceneStrategy
import cn.mengfly.compose_sample.common.getPackage
import cn.mengfly.compose_sample.common.pages.SampleDocument
import cn.mengfly.compose_sample.common.pages.SampleDocumentPage
import cn.mengfly.compose_sample.sample.TodoSample
import cn.mengfly.compose_sample.sample.aboutme.aboutMeSampleList
import cn.mengfly.compose_sample.sample.component.basicComponentSampleList
import cn.mengfly.compose_sample.sample.navigation3.navigation3Samples
import cn.mengfly.compose_sample.ui.theme.ComposeSampleTheme
import kotlinx.serialization.Serializable

@Serializable
class Sample(
    val title: String,
    val description: String? = null,
    val articleUrl: String? = null,
    val source: List<String> = emptyList(),
    val content: @Composable Sample.() -> Unit = { TodoSample(this) },
    val sourcePackage: String = getPackage()
) : NavKey

@Serializable
private data object SampleList : NavKey

class Heading(val title: String)

private val samples = listOf(
    *basicComponentSampleList,
    *navigation3Samples,
    *aboutMeSampleList
)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navBackStack = rememberNavBackStack(SampleList)

            ComposeSampleTheme {
                NavDisplay(
                    backStack = navBackStack,
                    onBack = {
                        navBackStack.removeLastOrNull()
                    },
                    sceneStrategy = SampleDocumentSceneStrategy(),
                    entryProvider = entryProvider {
                        entry<SampleList> {
                            SampleListView(
                                onSampleSelected = {
                                    navBackStack.add(it)
                                    // 同时，将文档的元数据传递给文档场景
                                    val document = SampleDocument(it)
                                    if (document.tabs().isNotEmpty()) {
                                        navBackStack.add(document)
                                    }
                                }
                            )
                        }
                        entry<Sample>(
                            metadata = SampleDocumentSceneStrategy.sample()
                        ) { sample ->
                            sample.content(sample)
                        }
                        entry<SampleDocument>(
                            metadata = SampleDocumentSceneStrategy.document()
                        ) {
                            SampleDocumentPage(it)
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SampleListView(onSampleSelected: (Sample) -> Unit) {
    ComposeSampleTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text("Compose Sample") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        ) { innerPadding ->
            SampleList(
                modifier = Modifier.padding(innerPadding),
                onSampleSelected = onSampleSelected,
            )
        }
    }
}

@Composable
private fun SampleList(modifier: Modifier = Modifier,
                       onSampleSelected: (Sample) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(samples) {
            when (it) {
                is Heading -> {
                    ListItem(
                        headlineContent = {
                            Text(
                                it.title,
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        modifier = Modifier.height(48.dp),
                        colors = ListItemDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                }

                is Sample -> {
                    ListItem(
                        headlineContent = { Text(it.title) },
                        supportingContent = {
                            it.description?.let { desc ->
                                Text(
                                    desc,
                                    maxLines = 2
                                )
                            }
                        },
//                        leadingContent = { Text(it.description) },
                        modifier = Modifier.clickable(onClick = dropUnlessResumed {
                            onSampleSelected(
                                it
                            )
                        })
                    )
                }
            }
        }
    }
}


@PreviewLightDark
@Composable
fun SampleListPreview() {
    ComposeSampleTheme {
        SampleList()
    }
}