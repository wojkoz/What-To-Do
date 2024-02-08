package com.example.whattodo.data.repository

import com.example.whattodo.data.local.entities.todos.TaskItemDao
import com.example.whattodo.data.mappers.task.toTaskItem
import com.example.whattodo.data.mappers.task.toTaskItemEntity
import com.example.whattodo.domain.models.SortBy.CreationDateDescending
import com.example.whattodo.domain.models.TaskItem
import com.example.whattodo.domain.repository.DataResult
import com.example.whattodo.domain.repository.todos.TaskItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TaskItemRepositoryImpl(private val taskItemDao: TaskItemDao) : TaskItemRepository {
    override suspend fun insert(item: TaskItem) {
        taskItemDao.insert(item.toTaskItemEntity())
    }

    override suspend fun delete(item: TaskItem) {
        taskItemDao.delete(item.toTaskItemEntity())
    }

    override suspend fun getByParentId(
        parentId: Long,
        sortBy: CreationDateDescending,
    ): Flow<DataResult<List<TaskItem>>> {
        return flow {
            emit(DataResult.Loading)
            val taskItems = taskItemDao.getByParentId(parentId).map { it.toTaskItem() }
            emit(DataResult.Success(taskItems))
        }
    }
}
