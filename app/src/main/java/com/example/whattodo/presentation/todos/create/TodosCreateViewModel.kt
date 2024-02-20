package com.example.whattodo.presentation.todos.create

import androidx.lifecycle.ViewModel
import com.example.whattodo.presentation.todos.create.TodosCreateEvent.OnContentChange
import com.example.whattodo.presentation.todos.create.TodosCreateEvent.OnCreateTask
import com.example.whattodo.presentation.todos.create.TodosCreateEvent.OnDateTimeChange
import com.example.whattodo.presentation.todos.create.TodosCreateEvent.OnPriorityChange
import com.example.whattodo.presentation.todos.create.TodosCreateEvent.OnTitleChange
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TodosCreateViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(TodosCreateState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: TodosCreateEvent) {
        when (event) {
            OnCreateTask -> {}
            is OnContentChange -> {}
            is OnDateTimeChange -> {}
            is OnPriorityChange -> {}
            is OnTitleChange -> {}
        }
    }
}