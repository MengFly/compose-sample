package cn.mengfly.compose_sample.common.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.mengfly.compose_sample.R
import cn.mengfly.compose_sample.common.DishManager


/**
 * 菜品列表页面
 *
 * @param onDishSelect 菜品选择回调
 */
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DishListPage(onDishSelect: (Int) -> Unit = {}) {
    val selectMapSaver: Saver<SnapshotStateMap<Int, Int>, *> = listSaver(
        save = { map ->
            map.entries.map { (key, value) -> listOf(key, value) }
        },
        restore = { list ->
            mutableStateMapOf(*list.map { it[0] to it[1] }.toTypedArray())
        }
    )
    // 记录菜品的选择状态
    val selectMap = rememberSaveable(saver = selectMapSaver) { mutableStateMapOf() }

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
