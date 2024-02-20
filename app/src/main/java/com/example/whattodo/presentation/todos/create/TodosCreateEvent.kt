package com.example.whattodo.presentation.todos.create

import com.example.whattodo.domain.models.task.item.TaskPriority

sealed class TodosCreateEvent {
    data object OnCreateTask : TodosCreateEvent()
    data class OnTitleChange(val title: String) : TodosCreateEvent()
    data class OnContentChange(val content: String) : TodosCreateEvent()
    data class OnDateTimeChange(val date: Long?, val hour: Int, val minute: Int) : TodosCreateEvent()
    data class OnPriorityChange(val priority: TaskPriority) : TodosCreateEvent()
}