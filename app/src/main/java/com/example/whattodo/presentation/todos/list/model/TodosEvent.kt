package com.example.whattodo.presentation.todos.list.model

import com.example.whattodo.domain.models.SortBy
import com.example.whattodo.domain.models.task.item.TaskItem
import com.example.whattodo.domain.models.task.list.TaskList

sealed class TodosEvent {
    data class OnTaskListSelect(val taskList: TaskList) : TodosEvent()
    data class OnTaskDone(val taskItem: TaskItem) : TodosEvent()
    data class OnTaskUnDone(val taskItem: TaskItem) : TodosEvent()
    data class OnTaskListCreate(val title: String, val setActive: Boolean) : TodosEvent()
    data object OnScreenStarted : TodosEvent()
    data class OnSortChange(val sortBy: SortBy) : TodosEvent()
}