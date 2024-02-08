package com.example.whattodo.domain.models

import java.time.LocalDateTime

data class TaskItem(
    val id: Long,
    val title: String,
    val content: String,
    val parentListId: Long,
    val isDone: Boolean,
    val createdAt: LocalDateTime,
    val validUntil: LocalDateTime,
    val isValid: Boolean,
    val priority: TaskPriority,
)
