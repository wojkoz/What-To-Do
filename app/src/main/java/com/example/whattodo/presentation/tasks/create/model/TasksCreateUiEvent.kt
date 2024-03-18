package com.example.whattodo.presentation.tasks.create.model

import com.example.whattodo.utils.UiText

sealed class TasksCreateUiEvent {
    data class ShowMessage(val message: UiText) : TasksCreateUiEvent()
    data object NavigateBack : TasksCreateUiEvent()
}