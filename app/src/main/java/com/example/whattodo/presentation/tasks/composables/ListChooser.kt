package com.example.whattodo.presentation.tasks.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.whattodo.R.string
import com.example.whattodo.domain.models.task.list.TaskList
import com.example.whattodo.ui.composables.ButtonText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListChooser(
    currentList: TaskList?,
    options: List<TaskList>,
    onListSelect: ((TaskList) -> Unit),
    onCreateNewListClick: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    var showListBottomSheet by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
    ) {
        if (showListBottomSheet) {
            ListChooserBottomSheet(
                sheetState = sheetState,
                options = options,
                onListSelect = {
                    showListBottomSheet = false
                    onListSelect.invoke(it)
                },
                onDismiss = {
                    showListBottomSheet = false
                },
                onCreateNewListClick = {
                    showListBottomSheet = false
                    onCreateNewListClick.invoke()
                }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(6.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            if (currentList == null) {
                Button(
                    onClick = { onCreateNewListClick.invoke() }
                ) {
                    ButtonText(text = stringResource(id = string.list_chooser_create_list))
                }
            } else {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(percent = 60)
                        )
                        .clickable {
                            showListBottomSheet = true
                        }
                        .padding(horizontal = 18.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ButtonText(text = currentList.title)
                        Image(
                            imageVector = Outlined.ArrowDropDown,
                            contentDescription = "Arrow",
                            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimary)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListChooserBottomSheet(
    sheetState: SheetState,
    options: List<TaskList>,
    onListSelect: (TaskList) -> Unit,
    onDismiss: () -> Unit,
    onCreateNewListClick: () -> Unit,
) {
    ModalBottomSheet(
        modifier = Modifier
            .height(300.dp),
        onDismissRequest = {
            onDismiss.invoke()
        },
        sheetState = sheetState
    ) {
        LazyColumn(modifier = Modifier.padding(bottom = 20.dp)) {
            item {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                ) {
                    Button(onClick = onCreateNewListClick) {
                        ButtonText(text = stringResource(id = string.list_chooser_create_list))
                    }
                }
            }
            items(count = options.size, key = { index -> options[index].id }) { index ->
                val item = options[index]
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onListSelect.invoke(item) },
                ) {
                    Text(
                        text = item.title,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewListChooserNullList() {
    ListChooser(
        currentList = null,
        options = emptyList(),
        onListSelect = {},
        onCreateNewListClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewListChooser() {
    ListChooser(
        currentList = TaskList(
            id = 0,
            title = "Default",
            isActive = true,
            doneTasksItems = listOf(),
            todoTasksItems = listOf()
        ),
        options = emptyList(),
        onListSelect = {},
        onCreateNewListClick = {},
    )
}
