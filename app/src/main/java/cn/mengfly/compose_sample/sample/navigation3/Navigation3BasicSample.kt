package cn.mengfly.compose_sample.sample.navigation3

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import cn.mengfly.compose_sample.R
import cn.mengfly.compose_sample.sample.common.Dish
import cn.mengfly.compose_sample.sample.common.DishManager
import cn.mengfly.compose_sample.ui.theme.ComposeSampleTheme
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable

/**
 * 欢迎页面Key
 */
@Serializable
data object Welcome : NavKey

/**
 * 主页Key
 */
@Serializable
data object Home : NavKey

/**
 * 菜品详情
 */
@Serializable
data class Detail(val id: Int) : NavKey

@Composable
fun Navigation3BasicSample() {

    ComposeSampleTheme {
        // 创建导航栈，通过该导航栈进行页面跳转控制
        val navBackStack = rememberNavBackStack(Welcome)

        NavDisplay(
            backStack = navBackStack,
            modifier = Modifier.fillMaxSize(),
            entryProvider = entryProvider {
                entry<Welcome> {
                    WelcomePage {
                        // 先移除掉欢迎页面，因为跳转后，欢迎页面将不再显示，不能点了返回之后又跳回到欢迎页面了
                        navBackStack.removeIf { it is Welcome }
                        // 将主页面添加到导航栈中
                        navBackStack.add(Home)
                    }
                }
                entry<Home> {
                    HomePage(onDishSelect = {
                        navBackStack.add(Detail(it))
                    })
                }

                entry<Detail> { detail ->
                    DishDetailPage(detail.id)
                }

            }
        )
    }
}

@Composable
fun DishDetailPage(id: Int) {
    var dish: Dish? by remember { mutableStateOf(null) }
    LaunchedEffect(id) {
        dish = DishManager.getDishById(id)
    }

    dish?.let { dish ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = dish.name,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = dish.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 8.dp)
            )
            // 可以在这里添加更多菜品详情信息，如价格、评分等
            Text(
                text = "价格: ¥${dish.price}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )

            Text(
                text = "配料: ${dish.ingredients.joinToString(", ")}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
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

/**
 * 主页
 */
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HomePage(onDishSelect: (Int) -> Unit = {}) {

    // 记录菜品的选择状态
    val selectMap = remember { mutableStateMapOf<Int, Int>() }

    Scaffold(
        Modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { Text(text = "我的厨房") }) },
        bottomBar = {
            if (selectMap.isNotEmpty()) {
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    )
                ) {
                    Row(
                        Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_kitchen),
                            tint = Color.Unspecified,
                            contentDescription = null,
                            modifier = Modifier
                                .size(48.dp)
                                .padding(6.dp)
                        )

                        Text(
                            text = "已选：${selectMap.values.sum()} 件菜品",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .weight(1f)

                        )

                        TextButton(
                            onClick = {},
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onBackground
                            )
                        ) {
                            Text(text = "去结算")
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(DishManager.dishes) {
                ListItem(
                    modifier = Modifier.clickable(onClick = {
                        onDishSelect(it.id)
                    }),
                    headlineContent = {
                        Text(text = it.name)
                    },
                    leadingContent = {
                        Icon(
                            painter = painterResource(id = it.thumbnail),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(64.dp)
                                .padding(6.dp)
                        )
                    },
                    supportingContent = {
                        Text(
                            text = it.description, maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    trailingContent = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (selectMap.containsKey(it.id)) {

                                Icon(
                                    painter = painterResource(id = R.drawable.ic_minus),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .padding(4.dp)
                                        .clickable(onClick = {
                                            selectMap[it.id] = selectMap.getOrDefault(it.id, 0) - 1
                                            if (selectMap[it.id] == 0) {
                                                selectMap.remove(it.id)
                                            }
                                        })
                                )
                                Text(
                                    text = selectMap[it.id]!!.toString(),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            Icon(
                                painter = painterResource(id = R.drawable.ic_add),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(30.dp)
                                    .padding(4.dp)
                                    .clickable(onClick = {
                                        selectMap[it.id] = selectMap.getOrDefault(it.id, 0) + 1
                                    })
                            )

                        }

                    }
                )
            }
        }
    }
}


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