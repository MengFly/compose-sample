package cn.mengfly.compose_sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import cn.mengfly.compose_sample.sample.TodoSample
import cn.mengfly.compose_sample.sample.navigation3.Navigation3BasicSample
import cn.mengfly.compose_sample.ui.theme.ComposeSampleTheme
import kotlinx.serialization.Serializable

@Serializable
class Sample(
    val title: String,
    val description: String? = null,
    val articleUrl: String? = null,
    val content: @Composable Sample.() -> Unit = { TodoSample(this) }
) : NavKey

@Serializable
private data object SampleList : NavKey

private class Heading(val title: String)

private val samples = listOf(
    Heading("Navigation3"),
    Sample(
        title = "Navigation3-Basic",
        description = "Navigation3基础使用方式",
        content = { Navigation3BasicSample() }
    ),

    Heading("About"),
    Sample(
        title = "我的公众号"
    )
)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navBackStack = rememberNavBackStack(SampleList)

            NavDisplay(
                backStack = navBackStack,
                onBack = {
                    navBackStack.removeLastOrNull()
                },
                entryProvider = entryProvider {
                    entry<SampleList> {
                        SampleListView {
                            navBackStack.add(it)
                        }
                    }
                    entry<Sample> { sample ->
                        sample.content(sample)
                    }
                },
                modifier = Modifier.fillMaxSize(),
            )


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
                onSampleSelected = onSampleSelected
            )
        }
    }
}

@Composable
private fun SampleList(modifier: Modifier = Modifier, onSampleSelected: (Sample) -> Unit = {}) {
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
                        supportingContent = { it.description?.let { desc -> Text(desc) } },
                        trailingContent = { Article(it) },
//                        leadingContent = { Text(it.description) },
                        modifier = Modifier.clickable(onClick = { onSampleSelected(it) })
                    )
                }
            }
        }
    }
}

@Composable
private fun Article(sample: Sample) {
    if (sample.articleUrl != null) {
        Icon(
            painter = painterResource(R.drawable.ic_article),
            contentDescription = "Article",
            tint = Color.Unspecified,
            modifier = Modifier
                .size(32.dp)
                .padding(4.dp)
                .clickable(onClick = {
                    /* TODO: open browser */

                })
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeSampleTheme {
        SampleList()
    }
}