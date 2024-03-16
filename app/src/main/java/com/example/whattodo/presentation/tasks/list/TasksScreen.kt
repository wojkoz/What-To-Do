package com.example.whattodo.presentation.tasks.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.whattodo.R
import com.example.whattodo.domain.models.SortBy
import com.example.whattodo.domain.models.task.item.TaskItem
import com.example.whattodo.presentation.tasks.composables.ListChooser
import com.example.whattodo.presentation.tasks.composables.TaskListCreator
import com.example.whattodo.presentation.tasks.composables.TaskListView
import com.example.whattodo.presentation.tasks.list.model.TasksEvent
import com.example.whattodo.presentation.tasks.list.model.TasksEvent.OnScreenStarted
import com.example.whattodo.presentation.tasks.list.model.TasksEvent.OnTaskDone
import com.example.whattodo.presentation.tasks.list.model.TasksEvent.OnTaskListCreate
import com.example.whattodo.presentation.tasks.list.model.TasksEvent.OnTaskListSelect
import com.example.whattodo.presentation.tasks.list.model.TasksEvent.OnTaskUnDone
import com.example.whattodo.presentation.tasks.list.model.TasksState
import com.example.whattodo.ui.composables.AppBar
import com.example.whattodo.ui.composables.CustomProgressIndicator

@Composable
fun TasksScreen(
    state: TasksState,
    onEvent: (TasksEvent) -> Unit,
    onNavigateToCreateTask: (parentListId: Long, taskId: Long?) -> Unit,
) {
    var showCreateTaskListDialog by remember { mutableStateOf(false) }
    var showSortByMenu by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        onEvent(OnScreenStarted)
    }

    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(id = R.string.main_screen_title),
                showBackIcon = false,
                actions = {
                    IconButton(onClick = { showSortByMenu = !showSortByMenu }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = stringResource(id = R.string.sort_by)
                        )
                        DropdownMenu(
                            expanded = showSortByMenu,
                            onDismissRequest = { showSortByMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                       Text(text = stringResource(id = R.string.sort_by_creation_date_ascending))
                                },
                                onClick = {
                                    onEvent(TasksEvent.OnSortChange(SortBy.CreationDateAscending))
                                    showSortByMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = stringResource(id = R.string.sort_by_creation_date_descending)) },
                                onClick = {
                                    onEvent(TasksEvent.OnSortChange(SortBy.CreationDateDescending))
                                    showSortByMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = stringResource(id = R.string.sort_by_valid_date_ascending)) },
                                onClick = {
                                    onEvent(TasksEvent.OnSortChange(SortBy.ValidDateAscending))
                                    showSortByMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = stringResource(id = R.string.sort_by_valid_date_descending)) },
                                onClick = {
                                    onEvent(TasksEvent.OnSortChange(SortBy.ValidDateDescending))
                                    showSortByMenu = false
                                }
                            )
                        }
                    }
                }
            )
                 },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Box {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
            ) {
                item {
                    ListChooser(
                        currentList = state.activeTaskList,
                        options = state.taskLists,
                        onListSelect = { onEvent(OnTaskListSelect(it)) },
                        onCreateNewListClick = { showCreateTaskListDialog = true }
                    )
                }

                item {
                    // to do listView
                    TaskListView(
                        title = stringResource(id = R.string.todo_list),
                        items = state.todoTaskItemsList,
                        onAddTaskClick = { taskItemId ->
                            state.activeTaskList?.let { parentList ->
                                onNavigateToCreateTask(parentList.id, taskItemId)
                            }
                        },
                        onTaskDone = { taskItem: TaskItem ->
                            onEvent(OnTaskDone(taskItem))
                        },
                        modifier = Modifier
                            .background(color = MaterialTheme.colorScheme.background)
                            .padding(horizontal = 10.dp)
                    )
                }

                item {
                    // done listView
                    TaskListView(
                        title = stringResource(id = R.string.done_list),
                        items = state.doneTaskItemsList,
                        onAddTaskClick = {},
                        onTaskDone = { taskItem: TaskItem ->
                            onEvent(OnTaskUnDone(taskItem))
                        },
                        showAddButton = false,
                        modifier = Modifier
                            .background(color = MaterialTheme.colorScheme.background)
                            .padding(horizontal = 10.dp)
                    )
                }
            }

            if (showCreateTaskListDialog) {
                TaskListCreator(
                    onDismiss = { showCreateTaskListDialog = false },
                    onConfirmation = { title: String, setActive: Boolean ->
                        showCreateTaskListDialog = false
                        onEvent(OnTaskListCreate(title, setActive))
                    },
                    shouldForceSetActive = state.activeTaskList == null
                )
            }

            if (state.isLoading) {
                CustomProgressIndicator()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    TasksScreen(
        onNavigateToCreateTask = { _, _ -> },
        state = TasksState(),
        onEvent = {}
    )
}
