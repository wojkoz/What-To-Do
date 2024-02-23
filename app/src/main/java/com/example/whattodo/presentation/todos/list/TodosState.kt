package com.example.whattodo.presentation.todos.list

import com.example.whattodo.domain.models.task.item.TaskItem
import com.example.whattodo.domain.models.task.list.TaskList

data class TodosState(
    val isLoading: Boolean = true,
    val taskLists: List<TaskList> = emptyList(),
    val activeTaskList: TaskList? = null,
    val todoTaskItemsList: List<TaskItem> = emptyList(),
    val doneTaskItemsList: List<TaskItem> = emptyList(),
)
