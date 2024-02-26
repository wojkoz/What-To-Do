package com.example.whattodo.domain.usecase.task.item

import com.example.whattodo.domain.models.task.item.TaskItem
import com.example.whattodo.domain.repository.todos.TaskItemRepository

class TaskUnDoneUseCase(
    private val taskItemRepository: TaskItemRepository,
) {
    suspend operator fun invoke(
        taskItem: TaskItem,
    ): TaskItem {
        val doneTaskItem = taskItem.copy(isDone = false)
        taskItemRepository.insert(doneTaskItem)
        return doneTaskItem
    }
}