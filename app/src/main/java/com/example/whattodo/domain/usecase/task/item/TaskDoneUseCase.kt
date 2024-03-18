package com.example.whattodo.domain.usecase.task.item

import com.example.whattodo.domain.models.task.item.TaskItem
import com.example.whattodo.domain.repository.tasks.TaskItemRepository

class TaskDoneUseCase(
    private val taskItemRepository: TaskItemRepository,
) {
    suspend operator fun invoke(
        taskItem: TaskItem,
    ): TaskItem {
        val doneTaskItem = taskItem.copy(isDone = true)
        taskItemRepository.insert(doneTaskItem)
        return doneTaskItem
    }
}