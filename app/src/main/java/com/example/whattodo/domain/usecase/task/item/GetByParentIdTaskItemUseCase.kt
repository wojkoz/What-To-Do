package com.example.whattodo.domain.usecase.task.item

import com.example.whattodo.domain.models.SortBy
import com.example.whattodo.domain.models.task.item.TaskItem
import com.example.whattodo.domain.repository.DataResult
import com.example.whattodo.domain.repository.tasks.TaskItemRepository
import kotlinx.coroutines.flow.Flow

class GetByParentIdTaskItemUseCase(
    private val taskItemRepository: TaskItemRepository,
) {
    suspend operator fun invoke(
        parentId: Long,
        sortBy: SortBy,
    ): Flow<DataResult<List<TaskItem>>> {
        return taskItemRepository.getByParentId(parentId = parentId, sortBy = sortBy)
    }
}