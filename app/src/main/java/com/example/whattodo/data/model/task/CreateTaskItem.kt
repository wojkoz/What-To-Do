package com.example.whattodo.data.model.task

import com.example.whattodo.domain.models.task.item.TaskPriority
import java.time.LocalDateTime

data class CreateTaskItem(
    val title: String,
    val content: String,
    val parentListId: Long,
    val validUntil: LocalDateTime,
    val priority: TaskPriority,
)
