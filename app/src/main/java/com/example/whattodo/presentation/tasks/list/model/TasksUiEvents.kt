package com.example.whattodo.presentation.tasks.list.model

sealed class TasksUiEvents {
    data class OpenFileSaveDialog(val json: String) : TasksUiEvents()
}