package com.example.whattodo.presentation.todos.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whattodo.domain.models.SortBy
import com.example.whattodo.domain.models.task.item.TaskItem
import com.example.whattodo.domain.models.task.list.TaskList
import com.example.whattodo.domain.repository.DataResult
import com.example.whattodo.domain.repository.DataResult.Error
import com.example.whattodo.domain.repository.DataResult.Loading
import com.example.whattodo.domain.repository.DataResult.Success
import com.example.whattodo.domain.usecase.task.TaskItemUseCases
import com.example.whattodo.domain.usecase.task.TaskListUseCases
import com.example.whattodo.presentation.todos.list.model.TodosEvent
import com.example.whattodo.presentation.todos.list.model.TodosEvent.OnScreenStarted
import com.example.whattodo.presentation.todos.list.model.TodosEvent.OnTaskDone
import com.example.whattodo.presentation.todos.list.model.TodosEvent.OnTaskListCreate
import com.example.whattodo.presentation.todos.list.model.TodosEvent.OnTaskListSelect
import com.example.whattodo.presentation.todos.list.model.TodosEvent.OnTaskUnDone
import com.example.whattodo.presentation.todos.list.model.TodosState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodosViewModel @Inject constructor(
    private val taskListUseCases: TaskListUseCases,
    private val taskItemUseCases: TaskItemUseCases,
) : ViewModel() {

    private val _uiState: MutableStateFlow<TodosState> = MutableStateFlow(TodosState())
    val uiState = _uiState.asStateFlow()

    private var sortBy: SortBy = SortBy.ValidDateDescending

    fun onEvent(event: TodosEvent) {
        when (event) {
            is OnTaskDone -> onTaskDone(event.taskItem)
            is OnTaskListSelect -> onTaskListSelect(event.taskList)
            is OnTaskListCreate -> onTaskListCreate(event.title, event.setActive)
            OnScreenStarted -> onScreenStarted()
            is OnTaskUnDone -> onTaskUnDone(event.taskItem)
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
            _uiState.update { state ->
                state.copy(
                    todoTaskItemsList = todoList,
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
            _uiState.update { state ->
                state.copy(
                    todoTaskItemsList = todoList,
                    doneTaskItemsList = doneList,
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
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                activeTaskList = activeTaskList,
                                taskLists = taskLists,
                                doneTaskItemsList = activeTaskList?.doneTasksItems.orEmpty(),
                                todoTaskItemsList = activeTaskList?.todoTasksItems.orEmpty()
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