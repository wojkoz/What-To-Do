package com.example.whattodo.presentation.todos.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.whattodo.R
import com.example.whattodo.presentation.todos.list.TodosEvent.OnTaskListCreate
import com.example.whattodo.presentation.todos.list.TodosEvent.OnTaskListSelect
import com.example.whattodo.presentation.todos.composables.ListChooser
import com.example.whattodo.presentation.todos.composables.TaskListCreator
import com.example.whattodo.ui.composables.AppBar
import com.example.whattodo.ui.composables.CustomProgressIndicator

@Composable
fun TodosScreen(
    viewModel: TodosViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showCreateTaskListDialog by remember { mutableStateOf(false) }
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
                        onListSelect = { viewModel.onEvent(OnTaskListSelect(it)) },
                        onCreateNewListClick = { showCreateTaskListDialog = true }
                    )
                }

                item {
                    // to do listView
                }
            }

            if (showCreateTaskListDialog) {
                TaskListCreator(
                    onDismiss = { showCreateTaskListDialog = false },
                    onConfirmation = { title: String, setActive: Boolean ->
                        showCreateTaskListDialog = false
                        viewModel.onEvent(OnTaskListCreate(title, setActive))
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
    TodosScreen()
}
