package com.example.whattodo.presentation.tasks.list.model

import com.example.whattodo.domain.models.task.item.TaskItem
import com.example.whattodo.domain.models.task.list.TaskList

data class TasksState(
    val isLoading: Boolean = true,
    val taskLists: List<TaskList> = emptyList(),
    val activeTaskList: TaskList? = null,
    val todoTaskItemsList: List<TaskItem> = emptyList(),
    val doneTaskItemsList: List<TaskItem> = emptyList(),
)
