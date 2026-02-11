package cn.mengfly.compose_sample.sample.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun ScaffoldSample() {

    // 定义 snackbarHostState，通过操作该对象控制 SnackBar 是否显示
    // 在 Compose 中，所有的组件都是函数，而函数不像对象那样可以调用 .show() 这类方法实现显示的效果
    // 因此，必须通过给函数传入控制对象来操作页面，这也是Compose的设计哲学，数据驱动页面。
    val snackbarHostState = remember { SnackbarHostState() }

    // 协程作用域，Compose组件内的协程要和组件的生命周期保持一致，通过这种方式可以获取一个与组件生命周期一致的协程作用域
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            // Compose 提供的标题栏，也可以编写任意的Compose组件，达到自定义标题栏的目的
            TopAppBar(
                title = {
                    Text(text = "Scaffold TopBar")
                },
//                windowInsets = WindowInsets(0, 0, 0, 0),
                // 向外扩展的高度，类似于paddingTop 和 paddingBottom
                expandedHeight = 50.dp,
                // 标题左侧的图标
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(10.dp),
                        imageVector = Icons.Rounded.Menu, contentDescription = null
                    )
                },
                // 右侧的菜单按钮
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    // 该导航按钮是否被选中
                    selected = true,
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(text = "Home")
                    },
                    onClick = {
                        // 可以通过这个点击事件切换选择的导航按钮
                    }
                )
                NavigationBarItem(
                    selected = false,
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Class,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(text = "Class")
                    },
                    onClick = {

                    }
                )
                NavigationBarItem(
                    selected = false,
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(text = "Profile")
                    },
                    onClick = {

                    }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // 点击按钮后，显示Snackbar
                scope.launch {
                    snackbarHostState.showSnackbar(
                        // 要显示的信息
                        "Snackbar",
                        // 显示的Action按钮，不填写不显示
                        actionLabel = "Undo",
                        // 显示时长, Indefinite 表示永久显示直到手动关闭或下一次显示
                        duration = SnackbarDuration.Short,
                        // 是否显示关闭按钮
                        withDismissAction = true
                    ).let { result ->
                        if (result == SnackbarResult.ActionPerformed) {
                            // 点击了按钮
                        } else {
                            // 没有点击按钮直到消失，或点击了关闭按钮
                        }
                    }
                }
            }) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
            }
        },
        snackbarHost = {
            // SnackBar 页面
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data
                )
            }
        }
    ) { innerPadding ->
        // 通过设置padding为contentPadding，避免Content内容被 topBar 与 bottomBar 遮盖
        Column(
            Modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.padding(innerPadding),
                text = "Hello Scaffold"
            )
        }

    }
}