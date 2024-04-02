package com.example.whattodo.presentation.tasks.list

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.whattodo.R
import com.example.whattodo.domain.models.SortBy
import com.example.whattodo.domain.models.task.item.TaskItem
import com.example.whattodo.presentation.tasks.composables.ImportTasksSettingsDialog
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
import com.example.whattodo.presentation.tasks.list.model.TasksUiEvents
import com.example.whattodo.presentation.tasks.list.model.TasksUiEvents.OpenFileSaveDialog
import com.example.whattodo.presentation.tasks.list.model.TasksUiEvents.ShowImportSettingsDialog
import com.example.whattodo.presentation.tasks.list.model.TasksUiEvents.ShowMessage
import com.example.whattodo.ui.composables.AppBar
import com.example.whattodo.ui.composables.CustomProgressIndicator
import com.example.whattodo.ui.composables.ExportOrImportTasksDialog
import com.example.whattodo.utils.files.alterDocument
import com.example.whattodo.utils.files.readTextFromUri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Composable
fun TasksScreen(
    state: TasksState,
    onEvent: (TasksEvent) -> Unit,
    onNavigateToCreateTask: (parentListId: Long, taskId: Long?) -> Unit,
    onUiEvent: Flow<TasksUiEvents>,
) {
    var showCreateTaskListDialog by remember { mutableStateOf(false) }
    var showSortByMenu by remember { mutableStateOf(false) }
    var showImportOrExportDialog by remember { mutableStateOf(false) }
    var showImportTasksSettingsDialog by remember { mutableStateOf(false) }
    var json: String? by remember {
        mutableStateOf(null)
    }
    val context = LocalContext.current
    val fileSaveLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) {
        it?.let { uri ->
            alterDocument(
                uri = uri,
                context = context,
                json = json ?: "",
                onError = { errorText ->
                    showImportOrExportDialog = false
                    Toast.makeText(context, errorText, Toast.LENGTH_LONG).show()
                },
                onSuccess = {
                    showImportOrExportDialog = false
                    Toast.makeText(context, R.string.successfully_exported_file, Toast.LENGTH_LONG).show()
                }
            )
            json = null
        }
    }
    val openFileLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) {
        it?.let { uri ->
            showImportOrExportDialog = false
            onEvent(TasksEvent.OnImportTasks(json = readTextFromUri(uri = uri, context = context)))
        }
    }

    LaunchedEffect(key1 = true) {
        onEvent(OnScreenStarted)
    }

    LaunchedEffect(key1 = true) {
        onUiEvent.collect { event ->
            when (event) {
                is OpenFileSaveDialog -> {
                    json = event.json
                    fileSaveLauncher.launch("db.json")
                }

                is ShowMessage -> {
                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_LONG).show()
                }

                ShowImportSettingsDialog -> showImportTasksSettingsDialog = true
            }
        }
    }

    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(id = R.string.main_screen_title),
                showBackIcon = false,
                actions = {
                    IconButton(onClick = { showImportOrExportDialog = true }) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = stringResource(id = R.string.export_tasks),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
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
                    Spacer(modifier = Modifier.height(20.dp))
                }

                item {
                    ListChooser(
                        currentList = state.activeTaskList,
                        options = state.taskLists,
                        onListSelect = { onEvent(OnTaskListSelect(it)) },
                        onCreateNewListClick = { showCreateTaskListDialog = true }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
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
                            .padding(horizontal = 10.dp)
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
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
                            .padding(horizontal = 10.dp)
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
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

            if (showImportOrExportDialog) {
                ExportOrImportTasksDialog(
                    onImport = { openFileLauncher.launch(arrayOf("application/json")) },
                    onExport = { onEvent(TasksEvent.OnExportTasksClick) },
                    onDismiss = { showImportOrExportDialog = false },
                )
            }

            if (showImportTasksSettingsDialog) {
                ImportTasksSettingsDialog(
                    onDismiss = {
                        showImportTasksSettingsDialog = false
                        onEvent(TasksEvent.OnImportTasksDismiss)
                    },
                    onImport = {
                        onEvent(TasksEvent.OnImportTasksSettingsSelected(option = it))
                    }
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
        onEvent = {},
        onUiEvent = flow { },
    )
}
