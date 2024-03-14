package com.example.whattodo.data.mappers.task

import com.example.whattodo.data.local.entities.tasks.TaskItemEntity
import com.example.whattodo.data.model.task.CreateTaskItem
import com.example.whattodo.domain.models.task.item.TaskItem
import com.example.whattodo.domain.models.task.item.TaskPriority
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

fun TaskItem.toTaskItemEntity(): TaskItemEntity {
    return TaskItemEntity(
        id = this.id,
        title = this.title,
        content = this.content,
        parentListId = this.parentListId,
        isDone = this.isDone,
        createdAt = this.createdAt.format(formatter),
        validUntil = this.validUntil.format(formatter),
        priority = this.priority.priorityAsInt,
    )
}

fun TaskItemEntity.toTaskItem(): TaskItem {
    val createdAt = LocalDateTime.parse(this.createdAt, formatter)
    val validUntil = LocalDateTime.parse(this.validUntil, formatter)
    val isValid = LocalDateTime.now() > validUntil
    return TaskItem(
        id = this.id,
        title = this.title,
        content = this.content,
        parentListId = this.parentListId,
        isDone = this.isDone,
        createdAt = createdAt,
        validUntil = validUntil,
        isValid = isValid,
        priority = TaskPriority.fromInt(this.priority),
    )
}

fun CreateTaskItem.toTaskItemEntity(): TaskItemEntity {
    return TaskItemEntity(
        title = this.title,
        content = this.content,
        parentListId = this.parentListId,
        isDone = false,
        createdAt = LocalDateTime.now().format(formatter),
        validUntil = this.validUntil.format(formatter),
        priority = this.priority.priorityAsInt,
    )
}