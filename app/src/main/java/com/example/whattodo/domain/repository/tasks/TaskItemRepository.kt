package com.example.whattodo.domain.repository.tasks

import com.example.whattodo.data.model.task.CreateTaskItem
import com.example.whattodo.domain.models.SortBy
import com.example.whattodo.domain.models.task.item.TaskItem
import com.example.whattodo.domain.repository.DataResult
import kotlinx.coroutines.flow.Flow

interface TaskItemRepository {
    suspend fun insert(item: TaskItem)

    suspend fun insert(item: CreateTaskItem)

    suspend fun delete(item: TaskItem)

    suspend fun getByParentId(
        parentId: Long,
        sortBy: SortBy,
    ): Flow<DataResult<List<TaskItem>>>

    suspend fun getById(
        id: Long,
    ): DataResult<TaskItem>
}