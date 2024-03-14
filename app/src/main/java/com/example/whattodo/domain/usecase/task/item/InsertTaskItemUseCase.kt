package com.example.whattodo.domain.usecase.task.item

import com.example.whattodo.domain.models.task.item.TaskItem
import com.example.whattodo.domain.repository.tasks.TaskItemRepository

class InsertTaskItemUseCase(
    private val taskItemRepository: TaskItemRepository,
) {
    suspend operator fun invoke(taskItem: TaskItem) {
        taskItemRepository.insert(taskItem)
    }
}