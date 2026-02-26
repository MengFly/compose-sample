package cn.mengfly.compose_sample.common.pages

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation3.runtime.NavKey
import cn.mengfly.compose_sample.R
import cn.mengfly.compose_sample.Sample
import cn.mengfly.compose_sample.sample.aboutme.aboutMeSampleList
import kotlinx.serialization.Serializable
import kotlin.math.sqrt

private const val GITHUB_PREFIX =
    "https://github.com/MengFly/compose-sample/blob/master/app/src/main/java/cn/mengfly/compose_sample/sample"

/**
 * 文档
 */
@Serializable
data class SampleDocument(val sample: Sample) : NavKey {
    internal fun tabs() = mutableListOf<SampleDocumentTab>().apply {
        sample.articleUrl?.let {
            add(SampleDocumentTab(R.drawable.ic_article, "文章", it))
        }
        sample.source.forEach {
            add(
                SampleDocumentTab(
                    R.drawable.ic_code, it,
                    "$GITHUB_PREFIX/${sample.sourcePackage}/${it}"
                )
            )
        }
    }.toList()
}

internal data class SampleDocumentTab(
    @field:DrawableRes val icon: Int,
    val title: String, val url: String
)

/**
 * 样例文档页面
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun SampleDocumentPage(document: SampleDocument) {
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    val tabs = document.tabs()
    var loadProgress by remember { mutableFloatStateOf(100f) }

    Scaffold(
        Modifier
            .fillMaxSize()
    ) {
        Card(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PrimaryTabRow(
                    selectedTabIndex = selectedIndex
                ) {
                    tabs.forEachIndexed { index, tab ->
                        Tab(selected = selectedIndex == index, onClick = {
                            selectedIndex = index
                        }) {
                            Icon(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .size(20.dp),
                                painter = painterResource(id = tab.icon),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                            Text(
                                modifier = Modifier.padding(bottom = 8.dp),
                                text = tab.title,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                if (loadProgress < 100) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        progress = { (sqrt(loadProgress) * 10f / 100f) })
                }
                @Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            settings.javaScriptEnabled = true
                            settings.loadWithOverviewMode = true
                            settings.useWideViewPort = true
                            settings.domStorageEnabled = true
                            settings.setSupportZoom(true)
                            webChromeClient = object : WebChromeClient() {
                                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                    loadProgress = newProgress.toFloat()
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    update = { webView -> webView.loadUrl(tabs[selectedIndex].url) })
            }


        }
    }

}

@Preview
@Composable
fun SampleDocumentPagePreview() {
    SampleDocumentPage(SampleDocument(aboutMeSampleList[1] as Sample))
}