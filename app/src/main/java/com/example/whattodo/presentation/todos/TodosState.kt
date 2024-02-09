package com.example.whattodo.presentation.todos

import com.example.whattodo.domain.models.task.item.TaskItem
import com.example.whattodo.domain.models.task.list.TaskList

data class TodosState(
    val isLoading: Boolean = true,
    val taskLists: List<TaskList> = listOf(
        TaskList(id = 0, isActive = true, doneTasksCount = 0, allTasksCount = 0, todoTasksCount = 0, title = "extra title"),
        TaskList(id = 1, isActive = false, doneTasksCount = 0, allTasksCount = 0, todoTasksCount = 0, title = "extra title2"),
        TaskList(id = 2, isActive = false, doneTasksCount = 0, allTasksCount = 0, todoTasksCount = 0, title = "extra title3"),
        TaskList(id = 3, isActive = false, doneTasksCount = 0, allTasksCount = 0, todoTasksCount = 0, title = "extra title4"),
        TaskList(id = 4, isActive = false, doneTasksCount = 0, allTasksCount = 0, todoTasksCount = 0, title = "extra title5"),
    ),//emptyList(),
    val activeTaskList: TaskList? = TaskList(id = 0, isActive = true, doneTasksCount = 0, allTasksCount = 0, todoTasksCount = 0, title = "extra title"),
    val todoTaskItemsList: List<TaskItem> = emptyList(),
    val doneTaskItemsList: List<TaskItem> = emptyList(),
)
