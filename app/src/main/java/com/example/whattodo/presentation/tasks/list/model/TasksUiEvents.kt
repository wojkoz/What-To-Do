package com.example.whattodo.presentation.tasks.list.model

import com.example.whattodo.utils.UiText

sealed class TasksUiEvents {
    data class OpenFileSaveDialog(val json: String) : TasksUiEvents()
    data class ShowMessage(val message: UiText) : TasksUiEvents()
    data object ShowImportSettingsDialog : TasksUiEvents()
}