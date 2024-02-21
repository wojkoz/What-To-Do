package com.example.whattodo.domain.usecase.task.item

import com.example.whattodo.domain.models.task.item.TaskItem
import com.example.whattodo.domain.repository.DataResult
import com.example.whattodo.domain.repository.todos.TaskItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetTaskItemByIdUseCase(
    private val taskItemRepository: TaskItemRepository,
) {
    suspend operator fun invoke(
        id: Long,
    ): Flow<DataResult<TaskItem>> {
        return flow {
            emit(DataResult.Loading)
            emit(taskItemRepository.getById(id = id))
        }
    }
}