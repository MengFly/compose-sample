package cn.mengfly.compose_sample.common.pages

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.navigation3.runtime.NavKey
import cn.mengfly.compose_sample.R
import kotlinx.serialization.Serializable

@Serializable
data class PictureDetail(@param:DrawableRes val picture: Int) : NavKey

@Composable
fun PictureDetailPage(@DrawableRes picture: Int) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Icon(
            painter = painterResource(id = picture), contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@PreviewLightDark
@Composable
fun PictureDetailSample() {
    PictureDetailPage(picture = R.drawable.ic_open)
}


