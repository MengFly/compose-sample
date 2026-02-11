package cn.mengfly.compose_sample.sample.aboutme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.mengfly.compose_sample.R

@Composable
@PreviewFontScale
@PreviewScreenSizes
fun MyWechat() {

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.wechat),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(240.dp)
                )

                Text(
                    text = "贰柒BUG栈",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 32.sp
                    )
                )

                Text(
                    text = "微信扫一扫二维码，关注我的公众号\n配合公众号文章阅读源码更轻松",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(top = 16.dp)
                )


            }


        }
    }

}