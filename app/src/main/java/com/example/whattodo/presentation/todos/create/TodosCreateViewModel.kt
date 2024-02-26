package com.example.whattodo.presentation.todos.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whattodo.R
import com.example.whattodo.domain.models.task.item.TaskItem
import com.example.whattodo.domain.models.task.item.TaskPriority
import com.example.whattodo.domain.repository.DataResult.Error
import com.example.whattodo.domain.repository.DataResult.Loading
import com.example.whattodo.domain.repository.DataResult.Success
import com.example.whattodo.domain.usecase.task.TaskItemUseCases
import com.example.whattodo.presentation.todos.create.model.TodosCreateEvent.OnContentChange
import com.example.whattodo.presentation.todos.create.model.TodosCreateEvent.OnCreateTask
import com.example.whattodo.presentation.todos.create.model.TodosCreateEvent.OnDateTimeChange
import com.example.whattodo.presentation.todos.create.model.TodosCreateEvent.OnPriorityChange
import com.example.whattodo.presentation.todos.create.model.TodosCreateEvent.OnTitleChange
import com.example.whattodo.presentation.todos.create.model.TodosCreateUiEvent.NavigateBack
import com.example.whattodo.presentation.todos.create.model.TodosCreateUiEvent.ShowMessage
import com.example.whattodo.presentation.todos.create.model.TodosCreateEvent
import com.example.whattodo.presentation.todos.create.model.TodosCreateState
import com.example.whattodo.presentation.todos.create.model.TodosCreateUiEvent
import com.example.whattodo.utils.Route
import com.example.whattodo.utils.UiText
import com.example.whattodo.utils.extensions.getLocalDateFromMillis
import com.example.whattodo.utils.extensions.toStandardDate
import com.example.whattodo.utils.extensions.toStandardTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class TodosCreateViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val taskItemUseCases: TaskItemUseCases,
) : ViewModel() {

    private val maxTitleLength = 80
    private val maxContentLength = 250

    private val parentListId: Long = checkNotNull(
        savedStateHandle.get<String>(
            Route.normalizeParameter(Route.parentListIdParameter)
        )?.toLong()
    )

    private var taskItem: TaskItem? = null

    private val _uiEvent = Channel<TodosCreateUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _uiState = MutableStateFlow(TodosCreateState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            setTaskFromSavedStateHandle()
            initializeState()
        }
    }

    fun onEvent(event: TodosCreateEvent) {
        when (event) {
            OnCreateTask -> onCreateTask()
            is OnContentChange -> onContentChange(event.content)
            is OnDateTimeChange -> onDatetimeChange(event.date, event.hour, event.minute)
            is OnPriorityChange -> onPriorityChange(event.priority)
            is OnTitleChange -> onTitleChange(event.title)
        }
    }

    private fun onCreateTask() {
        viewModelScope.launch {
            if (_uiState.value.titleError != null || _uiState.value.contentError != null) {
                _uiEvent.send(ShowMessage(UiText.StringResource(R.string.oops_something_went_wrong)))
                return@launch
            }
            _uiState.update {
                it.copy(isLoading = true)
            }

            if(taskItem != null) {
                val editedTaskItem = taskItem?.copy(
                    title = _uiState.value.title,
                    content = _uiState.value.content,
                    parentListId = parentListId,
                    validUntil = _uiState.value.validUntil,
                    priority = _uiState.value.priority,
                )
                editedTaskItem?.let {
                    taskItemUseCases.insertTaskItemUseCase(it)
                }
            } else {
                taskItemUseCases.createTaskItemUseCase(
                    title = _uiState.value.title,
                    content = _uiState.value.content,
                    parentListId = parentListId,
                    validUntil = _uiState.value.validUntil,
                    priority = _uiState.value.priority,
                )
            }

            _uiState.update {
                it.copy(isLoading = false)
            }
            _uiEvent.send(NavigateBack)
        }
    }

    private fun onDatetimeChange(
        date: Long?,
        hour: Int,
        minute: Int,
    ) {
        viewModelScope.launch {
            val localDateTime = if (date == null) {
                LocalDateTime.of(
                    LocalDate.now().plusDays(1),
                    LocalTime.of(hour, minute)
                )
            } else {
                val localDate = getLocalDateFromMillis(date)
                LocalDateTime.of(
                    localDate,
                    LocalTime.of(hour, minute)
                )
            }
            _uiState.update {
                it.copy(
                    validUntil = localDateTime,
                    date = localDateTime.toStandardDate(),
                    time = localDateTime.toStandardTime(),
                )
            }
        }
    }

    private fun onPriorityChange(priority: TaskPriority) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(priority = priority)
            }
        }
    }

    private fun onTitleChange(newTitle: String) {
        viewModelScope.launch {
            val validationResult = taskItemUseCases.validateLengthUseCase(newTitle, maxTitleLength)
            _uiState.update {
                it.copy(
                    title = newTitle,
                    titleError = validationResult.errorMessage,
                )
            }
        }
    }

    private fun onContentChange(newContent: String) {
        viewModelScope.launch {
            val validationResult = taskItemUseCases.validateLengthUseCase(newContent, maxContentLength, minLength = 0)
            _uiState.update {
                it.copy(
                    content = newContent,
                    contentError = validationResult.errorMessage
                )
            }
        }
    }

    private suspend fun setTaskFromSavedStateHandle() {
        val id = savedStateHandle.get<String>(Route.normalizeParameter(Route.taskIdParameter))?.toLongOrNull()
        if (id == null || id == -1L) {
            taskItem = null
            return
        }
        taskItemUseCases.getTaskItemByIdUseCase(id).collect { result ->
            when (result) {
                is Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _uiEvent.send(ShowMessage(result.message))
                }

                Loading -> _uiState.update { it.copy(isLoading = true) }
                is Success -> {
                    _uiState.update { it.copy(isLoading = true) }
                    taskItem = result.data
                }
            }
        }
    }

    private fun initializeState() {
        val item = taskItem
        if (item != null) {
            _uiState.update {
                it.copy(
                    title = item.title,
                    isLoading = false,
                    time = item.validUntil.toStandardTime(),
                    date = item.validUntil.toStandardDate(),
                    priority = item.priority,
                    validUntil = item.validUntil,
                    content = item.content,
                    contentError = null,
                    titleError = null,
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    time = it.validUntil.toStandardTime(),
                    date = it.validUntil.toStandardDate(),
                )
            }
        }
    }
}