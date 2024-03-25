package com.example.whattodo.presentation.tasks.list.model

import com.example.whattodo.domain.models.SortBy
import com.example.whattodo.domain.models.task.item.TaskItem
import com.example.whattodo.domain.models.task.list.TaskList

sealed class TasksEvent {
    data class OnTaskListSelect(val taskList: TaskList) : TasksEvent()
    data class OnTaskDone(val taskItem: TaskItem) : TasksEvent()
    data class OnTaskUnDone(val taskItem: TaskItem) : TasksEvent()
    data class OnTaskListCreate(val title: String, val setActive: Boolean) : TasksEvent()
    data object OnScreenStarted : TasksEvent()
    data class OnSortChange(val sortBy: SortBy) : TasksEvent()
    data object OnExportTasksClick : TasksEvent()
    data object OnImportTasksClick : TasksEvent()
}