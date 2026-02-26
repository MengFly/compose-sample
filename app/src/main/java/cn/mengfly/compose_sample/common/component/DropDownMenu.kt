package cn.mengfly.compose_sample.common.component

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenu(
    label: String,
    options: List<String>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit
) {
    var menuExpand by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = menuExpand,
        onExpandedChange = { menuExpand = !menuExpand }
    ) {

        val selectValue = if (selectedIndex >= 0) {
            if (selectedIndex < options.size) {
                options[selectedIndex]
            } else {
                "未选择"
            }
        } else {
            "未选择"
        }
        TextField(
            readOnly = true,
            value = selectValue,
            onValueChange = { },
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = menuExpand
                )
            },
            modifier = Modifier.menuAnchor(
                ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                true
            )
        )

        ExposedDropdownMenu(
            expanded = menuExpand,
            onDismissRequest = { menuExpand = false }) {

            options.forEach {
                DropdownMenuItem(
                    text = { Text(text = it) },
                    onClick = {
                        menuExpand = false
                        onSelected(options.indexOf(it))
                    }
                )
            }
        }

    }


}