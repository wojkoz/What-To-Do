package com.example.whattodo.presentation.tasks.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whattodo.R
import com.example.whattodo.domain.models.SortBy
import com.example.whattodo.domain.models.task.item.TaskItem
import com.example.whattodo.domain.models.task.list.TaskList
import com.example.whattodo.domain.repository.DataResult
import com.example.whattodo.domain.repository.DataResult.Error
import com.example.whattodo.domain.repository.DataResult.Loading
import com.example.whattodo.domain.repository.DataResult.Success
import com.example.whattodo.domain.usecase.task.TaskItemUseCases
import com.example.whattodo.domain.usecase.task.TaskListUseCases
import com.example.whattodo.presentation.tasks.composables.ImportTasksSettings
import com.example.whattodo.presentation.tasks.composables.ImportTasksSettings.ADD_AS_NEW
import com.example.whattodo.presentation.tasks.composables.ImportTasksSettings.CLEAR_DB_AND_ADD
import com.example.whattodo.presentation.tasks.list.model.TasksEvent
import com.example.whattodo.presentation.tasks.list.model.TasksEvent.OnExportTasksClick
import com.example.whattodo.presentation.tasks.list.model.TasksEvent.OnImportTasks
import com.example.whattodo.presentation.tasks.list.model.TasksEvent.OnImportTasksDismiss
import com.example.whattodo.presentation.tasks.list.model.TasksEvent.OnImportTasksSettingsSelected
import com.example.whattodo.presentation.tasks.list.model.TasksEvent.OnScreenStarted
import com.example.whattodo.presentation.tasks.list.model.TasksEvent.OnSortChange
import com.example.whattodo.presentation.tasks.list.model.TasksEvent.OnTaskDone
import com.example.whattodo.presentation.tasks.list.model.TasksEvent.OnTaskListCreate
import com.example.whattodo.presentation.tasks.list.model.TasksEvent.OnTaskListSelect
import com.example.whattodo.presentation.tasks.list.model.TasksEvent.OnTaskUnDone
import com.example.whattodo.presentation.tasks.list.model.TasksState
import com.example.whattodo.presentation.tasks.list.model.TasksUiEvents
import com.example.whattodo.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskListUseCases: TaskListUseCases,
    private val taskItemUseCases: TaskItemUseCases,
) : ViewModel() {

    private val _uiEvent = Channel<TasksUiEvents>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _uiState: MutableStateFlow<TasksState> = MutableStateFlow(TasksState())
    val uiState = _uiState.asStateFlow()

    private var _sortBy: SortBy = SortBy.CreationDateDescending

    private var _importedTasksLists: List<TaskList>? = null

    fun onEvent(event: TasksEvent) {
        when (event) {
            is OnTaskDone -> onTaskDone(event.taskItem)
            is OnTaskListSelect -> onTaskListSelect(event.taskList)
            is OnTaskListCreate -> onTaskListCreate(event.title, event.setActive)
            OnScreenStarted -> onScreenStarted()
            is OnTaskUnDone -> onTaskUnDone(event.taskItem)
            is OnSortChange -> onSortChange(event.sortBy)
            OnExportTasksClick -> onExportTasks()
            is OnImportTasks -> onImportTasks(event.json)
            OnImportTasksDismiss -> onImportTasksDismiss()
            is OnImportTasksSettingsSelected -> onImportTasksSettingsSelected(event.option)
        }
    }

    private fun onImportTasksSettingsSelected(option: ImportTasksSettings) {
        viewModelScope.launch {
            val importedTasks = _importedTasksLists
            if (importedTasks != null) {
                when (option) {
                    CLEAR_DB_AND_ADD -> taskListUseCases.importAllTasksUseCase(importedTasks, clearDb = true)
                    ADD_AS_NEW -> taskListUseCases.importAllTasksUseCase(importedTasks, clearDb = false)
                }
                loadAllTaskLists()
            }
        }
    }

    private fun onImportTasksDismiss() {
        viewModelScope.launch {
            _importedTasksLists = null
        }
    }

    private fun onImportTasks(json: String?) {
        viewModelScope.launch {
            if (json == null) {
                _importedTasksLists = null
                _uiEvent.send(
                    TasksUiEvents.ShowMessage(
                        message = UiText.StringResource(R.string.error_could_not_parse_file)
                    )
                )
            } else {
                try {
                    _importedTasksLists = Json.decodeFromString<List<TaskList>>(json)
                    _uiEvent.send(TasksUiEvents.ShowImportSettingsDialog)
                } catch (e: SerializationException) {
                    Timber.d(e)
                    _uiEvent.send(
                        TasksUiEvents.ShowMessage(
                            message = UiText.StringResource(R.string.error_could_not_deserialize_json)
                        )
                    )
                } catch (e: IllegalArgumentException) {
                    Timber.d(e)
                    _uiEvent.send(
                        TasksUiEvents.ShowMessage(
                            message = UiText.StringResource(R.string.error_could_not_deserialize_json)
                        )
                    )
                }
            }
        }
    }

    private fun onExportTasks() {
        viewModelScope.launch {
            taskListUseCases.getAllTaskListUseCase().collect { result ->
                when (result) {
                    is Error -> onError()
                    Loading -> onLoading()
                    is Success -> {
                        result.data?.let {
                            val json = Json.encodeToString(it)
                            _uiEvent.send(TasksUiEvents.OpenFileSaveDialog(json))
                            _uiState.update { state ->
                                state.copy(isLoading = false)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onSortChange(sortBy: SortBy) {
        viewModelScope.launch {
            _sortBy = sortBy
            sortTasks(
                todoTasks = _uiState.value.todoTaskItemsList,
                doneTasks = _uiState.value.doneTaskItemsList,
                sortBy = _sortBy
            )
        }
    }

    private fun sortTasks(
        todoTasks: List<TaskItem>?,
        doneTasks: List<TaskItem>?,
        sortBy: SortBy,
    ) {
        _uiState.update { state ->
            state.copy(
                todoTaskItemsList = if (todoTasks != null) {
                    taskItemUseCases.sortTaskItemsUseCase(
                        todoTasks,
                        sortBy
                    )
                } else {
                    state.todoTaskItemsList
                },
                doneTaskItemsList = if (doneTasks != null) {
                    taskItemUseCases.sortTaskItemsUseCase(
                        doneTasks,
                        sortBy
                    )
                } else {
                    state.doneTaskItemsList
                },
            )
        }
    }

    private fun onTaskUnDone(taskItem: TaskItem) {
        viewModelScope.launch {
            onLoading()
            val undoneTaskItem = taskItemUseCases.taskUnDoneUseCase(taskItem)
            val doneList = _uiState.value.doneTaskItemsList.filterNot {
                it.id == undoneTaskItem.id
            }
            val todoList = _uiState.value.todoTaskItemsList.toMutableList()
            todoList.add(undoneTaskItem)
            sortTasks(
                todoTasks = todoList,
                doneTasks = null,
                sortBy = _sortBy
            )
            _uiState.update { state ->
                state.copy(
                    doneTaskItemsList = doneList,
                    isLoading = false,
                )
            }
        }
    }

    private fun onTaskDone(taskItem: TaskItem) {
        viewModelScope.launch {
            onLoading()
            val doneTaskItem = taskItemUseCases.taskDoneUseCase(taskItem)
            val todoList = _uiState.value.todoTaskItemsList.filterNot {
                it.id == doneTaskItem.id
            }
            val doneList = _uiState.value.doneTaskItemsList.toMutableList()
            doneList.add(doneTaskItem)
            sortTasks(
                todoTasks = null,
                doneTasks = doneList,
                sortBy = _sortBy
            )
            _uiState.update { state ->
                state.copy(
                    todoTaskItemsList = todoList,
                    isLoading = false,
                )
            }
        }
    }

    private fun onScreenStarted() {
        viewModelScope.launch {
            loadAllTaskLists()
        }
    }

    private suspend fun loadAllTaskLists() {
        taskListUseCases.getAllTaskListUseCase().collect { result: DataResult<List<TaskList>> ->
            when (result) {
                is Error -> onError()
                Loading -> onLoading()
                is Success -> {
                    result.data?.let { data ->
                        val activeTaskList = data.find { it.isActive }
                        val taskLists = data.filterNot { it.isActive }
                        sortTasks(
                            todoTasks = activeTaskList?.todoTasksItems.orEmpty(),
                            doneTasks = activeTaskList?.doneTasksItems.orEmpty(),
                            sortBy = _sortBy
                        )
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                activeTaskList = activeTaskList,
                                taskLists = taskLists,
                            )
                        }
                    }
                }
            }
        }
    }

    private fun onTaskListSelect(item: TaskList) {
        viewModelScope.launch {
            onLoading()
            _uiState.value.activeTaskList?.let {
                taskListUseCases.insertActiveTaskUseCase(it.copy(isActive = false))
            }
            taskListUseCases.insertActiveTaskUseCase(item.copy(isActive = true))
            loadAllTaskLists()
        }
    }

    private fun onTaskListCreate(
        title: String,
        setActive: Boolean,
    ) {
        viewModelScope.launch {
            onLoading()
            taskListUseCases.createTaskListUseCase(title = title, setActive = setActive)
            loadAllTaskLists()
        }
    }

    private fun onError() {
        _uiState.update { state -> state.copy(isLoading = false) }
    }

    private fun onLoading() {
        _uiState.update { state -> state.copy(isLoading = true) }
    }
}
