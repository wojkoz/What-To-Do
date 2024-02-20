package com.example.whattodo.presentation.todos.create

import com.example.whattodo.domain.models.task.item.TaskPriority
import java.time.LocalDateTime

data class TodosCreateState(
    val title: String = "",
    val content: String = "",
    val validUntil: LocalDateTime = LocalDateTime.now().plusDays(1),
    val priority: TaskPriority = TaskPriority.Low,
    val isLoading: Boolean = false,
    val time: String? = null,
    val date: String? = null,
)
