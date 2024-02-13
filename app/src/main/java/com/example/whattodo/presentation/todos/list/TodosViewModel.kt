package com.example.whattodo.presentation.todos.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whattodo.domain.models.task.list.TaskList
import com.example.whattodo.domain.repository.DataResult
import com.example.whattodo.domain.repository.DataResult.Error
import com.example.whattodo.domain.repository.DataResult.Loading
import com.example.whattodo.domain.repository.DataResult.Success
import com.example.whattodo.domain.usecase.task.TaskListUseCases
import com.example.whattodo.presentation.todos.list.TodosEvent.OnTaskDone
import com.example.whattodo.presentation.todos.list.TodosEvent.OnTaskListCreate
import com.example.whattodo.presentation.todos.list.TodosEvent.OnTaskListSelect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodosViewModel @Inject constructor(
    private val taskListUseCases: TaskListUseCases,
) : ViewModel() {

    private val _uiState: MutableStateFlow<TodosState> = MutableStateFlow(TodosState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            loadAllTaskLists()
        }
    }

    fun onEvent(event: TodosEvent) {
        when (event) {
            is OnTaskDone -> {}
            is OnTaskListSelect -> onTaskListSelect(event.taskList)
            is OnTaskListCreate -> onTaskListCreate(event.title, event.setActive)
        }
    }

    private suspend fun loadAllTaskLists() {
        taskListUseCases.getAllTaskListUseCase().collect { result: DataResult<List<TaskList>> ->
            when (result) {
                is Error -> onError(result.message)
                Loading -> onLoading()
                is Success -> {
                    result.data?.let { data ->
                        val activeTaskList = data.find { it.isActive }
                        val taskLists = data.filterNot { it.isActive }
                        Log.e("loadAllTaskLists", "$activeTaskList || $taskLists")
                        _uiState.update { state ->
                            state.copy(isLoading = false, activeTaskList = activeTaskList, taskLists = taskLists)
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

    private fun onError(message: String) {
        _uiState.update { state -> state.copy(isLoading = false) }
    }

    private fun onLoading() {
        _uiState.update { state -> state.copy(isLoading = true) }
    }
}