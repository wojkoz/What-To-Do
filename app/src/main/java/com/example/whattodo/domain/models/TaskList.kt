package com.example.whattodo.domain.models

/**
* Represents a task list.
*
* @property id The unique identifier of the task list.
* @property title The title of the task list.
* @property doneTasksCount The number of tasks marked as done in the task list. Defaults to 0.
* @property allTasksCount The total number of tasks in the task list. Defaults to 0.
* @property todoTasksCount The number of tasks that are yet to be done in the task list. Defaults to 0.
*/
data class TaskList(
    val id: Long,
    val title: String,
    val doneTasksCount: Int = 0,
    val allTasksCount: Int = 0,
    val todoTasksCount: Int = 0,
)
