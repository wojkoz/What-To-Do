package com.example.whattodo.presentation.todos.create.model

import com.example.whattodo.utils.UiText

sealed class TodosCreateUiEvent {
    data class ShowMessage(val message: UiText) : TodosCreateUiEvent()
    data object NavigateBack : TodosCreateUiEvent()
}