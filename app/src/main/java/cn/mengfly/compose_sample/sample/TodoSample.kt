package cn.mengfly.compose_sample.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cn.mengfly.compose_sample.Sample

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoSample(sample: Sample) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(sample.title) })
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            sample.description?.let {
                Text(
                    text = sample.description,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Text(
                text = "案例开发中...",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview
@Composable
fun TodoSamplePreview() {
    TodoSample(
        Sample(
            title = "Todo",
            description = "Todo",
            content = {
                TodoSample(this)
            }
        ))
}