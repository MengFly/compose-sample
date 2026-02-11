package cn.mengfly.compose_sample.common.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation3.runtime.NavKey
import cn.mengfly.compose_sample.common.Dish
import cn.mengfly.compose_sample.common.DishManager
import kotlinx.serialization.Serializable

/**
 * 菜品详情
 */
@Serializable
data class DishDetail(val id: Int) : NavKey

/**
 * 菜品详情页面
 * @param id 菜品Id
 */
@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DishDetailPage(id: Int = 1,
                   onDishPictureClick: (Int) -> Unit = {}
) {
    var dish: Dish? by remember { mutableStateOf(null) }
    LaunchedEffect(id) {
        dish = DishManager.getDishById(id)
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "菜品详情") })
        }
    ) { paddingValues ->
        dish?.let { dish ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues = paddingValues),
                verticalArrangement = Arrangement.spacedBy(8.dp),

                ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = dish.thumbnail),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .size(240.dp)
                            .clickable(onClick = dropUnlessResumed {
                                onDishPictureClick(
                                    dish.thumbnail
                                )
                            })
                    )

                    Text(
                        text = dish.name,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Text(
                        text = dish.description,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                Text(
                    text = "价格: ¥${dish.price}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Text(
                    text = "配料: ${dish.ingredients.joinToString(", ")}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        } ?: run {
            Text(
                text = "加载中...",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
