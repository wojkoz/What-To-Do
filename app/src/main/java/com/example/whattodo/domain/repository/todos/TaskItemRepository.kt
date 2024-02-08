package com.example.whattodo.domain.repository.todos

import com.example.whattodo.domain.models.SortBy
import com.example.whattodo.domain.models.TaskItem
import com.example.whattodo.domain.repository.DataResult
import kotlinx.coroutines.flow.Flow

interface TaskItemRepository {
    suspend fun insert(item: TaskItem)

    suspend fun delete(item: TaskItem)

    suspend fun getByParentId(
        parentId: Long,
        sortBy: SortBy.CreationDateDescending,
    ): Flow<DataResult<List<TaskItem>>>
}