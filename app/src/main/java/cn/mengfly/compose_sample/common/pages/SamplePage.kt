package cn.mengfly.compose_sample.common.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

@Composable
fun CommonPage(text: String, color: Color,
               modifier: Modifier = Modifier
) {
    Text(
        text = text, modifier = modifier
            .fillMaxSize()
            .background(color),
        textAlign = TextAlign.Center
    )
}