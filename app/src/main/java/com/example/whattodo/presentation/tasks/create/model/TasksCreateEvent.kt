package com.example.whattodo.presentation.tasks.create.model

import com.example.whattodo.domain.models.task.item.TaskPriority

sealed class TasksCreateEvent {
    data object OnCreateTask : TasksCreateEvent()
    data class OnTitleChange(val title: String) : TasksCreateEvent()
    data class OnContentChange(val content: String) : TasksCreateEvent()
    data class OnDateTimeChange(val date: Long?, val hour: Int, val minute: Int) : TasksCreateEvent()
    data class OnPriorityChange(val priority: TaskPriority) : TasksCreateEvent()
}