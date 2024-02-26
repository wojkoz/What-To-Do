package com.example.whattodo.presentation.todos.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.whattodo.R
import com.example.whattodo.presentation.todos.composables.ListChooser
import com.example.whattodo.presentation.todos.composables.TaskListCreator
import com.example.whattodo.presentation.todos.composables.TaskListView
import com.example.whattodo.presentation.todos.list.model.TodosEvent
import com.example.whattodo.presentation.todos.list.model.TodosEvent.OnScreenStarted
import com.example.whattodo.presentation.todos.list.model.TodosEvent.OnTaskListCreate
import com.example.whattodo.presentation.todos.list.model.TodosEvent.OnTaskListSelect
import com.example.whattodo.presentation.todos.list.model.TodosState
import com.example.whattodo.ui.composables.AppBar
import com.example.whattodo.ui.composables.CustomProgressIndicator

@Composable
fun TodosScreen(
    state: TodosState,
    onEvent: (TodosEvent) -> Unit,
    onNavigateToCreateTask: (parentListId: Long, taskId: Long?) -> Unit,
) {
    var showCreateTaskListDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        onEvent(OnScreenStarted)
    }

    Scaffold(
        topBar = { AppBar(title = stringResource(id = R.string.main_screen_title), showBackIcon = false) },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Box {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
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
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }

                item {
                    // done listView
                    TaskListView(
                        title = stringResource(id = R.string.done_list),
                        items = state.doneTaskItemsList,
                        onAddTaskClick = {},
                        showAddButton = false,
                        modifier = Modifier.padding(horizontal = 10.dp)
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
    TodosScreen(
        onNavigateToCreateTask = { _, _ -> },
        state = TodosState(),
        onEvent = {}
    )
}
