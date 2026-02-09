package cn.mengfly.compose_sample.common.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import cn.mengfly.compose_sample.R
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable

/**
 * 欢迎页面Key
 */
@Serializable
data object Welcome : NavKey

/**
 * 欢迎页面
 *
 * 该页面启动后，2秒后自动跳转，跳转时调用 onTimeout
 */
@Preview
@Composable
fun WelcomePage(onTimeout: () -> Unit = {}) {

    val currentTimeout by rememberUpdatedState(onTimeout)

    LaunchedEffect(Unit) {
        delay(2000)
        currentTimeout()
    }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_kitchen),
            contentDescription = null,
            tint = Color.Unspecified,
        )

        Text(
            text = "欢迎",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
}