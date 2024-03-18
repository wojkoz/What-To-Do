package com.example.whattodo.domain.usecase.task.item

import com.example.whattodo.data.model.task.CreateTaskItem
import com.example.whattodo.domain.models.task.item.TaskPriority
import com.example.whattodo.domain.repository.tasks.TaskItemRepository
import java.time.LocalDateTime

class CreateTaskItemUseCase(
    private val taskItemRepository: TaskItemRepository,
) {
    suspend operator fun invoke(
        title: String,
        content: String,
        parentListId: Long,
        validUntil: LocalDateTime,
        priority: TaskPriority,
    ) {
        val createTaskItem = CreateTaskItem(
            title = title,
            content = content,
            parentListId = parentListId,
            validUntil = validUntil,
            priority = priority,
        )

        taskItemRepository.insert(createTaskItem)
    }
}