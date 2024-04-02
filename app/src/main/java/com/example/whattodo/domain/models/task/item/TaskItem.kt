package com.example.whattodo.domain.models.task.item

import com.example.whattodo.domain.models.serializers.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class TaskItem(
    val id: Long,
    val title: String,
    val content: String,
    val parentListId: Long,
    val isDone: Boolean,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val validUntil: LocalDateTime,
    val isValid: Boolean,
    val priority: TaskPriority,
)
