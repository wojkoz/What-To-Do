package com.example.whattodo.data.repository

import com.example.whattodo.R
import com.example.whattodo.data.local.entities.todos.TaskItemDao
import com.example.whattodo.data.mappers.task.toTaskItem
import com.example.whattodo.data.mappers.task.toTaskItemEntity
import com.example.whattodo.data.model.task.CreateTaskItem
import com.example.whattodo.domain.models.SortBy
import com.example.whattodo.domain.models.SortBy.CreationDateAscending
import com.example.whattodo.domain.models.SortBy.CreationDateDescending
import com.example.whattodo.domain.models.SortBy.PriorityAscending
import com.example.whattodo.domain.models.SortBy.PriorityDescending
import com.example.whattodo.domain.models.SortBy.TitleAscending
import com.example.whattodo.domain.models.SortBy.TitleDescending
import com.example.whattodo.domain.models.SortBy.ValidDateAscending
import com.example.whattodo.domain.models.SortBy.ValidDateDescending
import com.example.whattodo.domain.models.task.item.TaskItem
import com.example.whattodo.domain.repository.DataResult
import com.example.whattodo.domain.repository.todos.TaskItemRepository
import com.example.whattodo.utils.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TaskItemRepositoryImpl(private val taskItemDao: TaskItemDao) : TaskItemRepository {
    override suspend fun insert(item: TaskItem) {
        taskItemDao.insert(item.toTaskItemEntity())
    }

    override suspend fun insert(item: CreateTaskItem) {
        taskItemDao.insert(item.toTaskItemEntity())
    }

    override suspend fun delete(item: TaskItem) {
        taskItemDao.delete(item.toTaskItemEntity())
    }

    override suspend fun getByParentId(
        parentId: Long,
        sortBy: SortBy,
    ): Flow<DataResult<List<TaskItem>>> {
        return flow {
            emit(DataResult.Loading)
            val taskItems = taskItemDao.getByParentId(parentId).map { it.toTaskItem() }
            val sortedTasks = when (sortBy) {
                CreationDateAscending -> taskItems.sortedBy { it.createdAt }
                CreationDateDescending -> taskItems.sortedByDescending { it.createdAt }
                PriorityAscending -> taskItems.sortedBy { it.priority.priorityAsInt }
                PriorityDescending -> taskItems.sortedByDescending { it.priority.priorityAsInt }
                TitleAscending -> taskItems.sortedBy { it.title }
                TitleDescending -> taskItems.sortedByDescending { it.title }
                ValidDateAscending -> taskItems.sortedBy { it.validUntil }
                ValidDateDescending -> taskItems.sortedByDescending { it.validUntil }
            }
            emit(DataResult.Success(sortedTasks))
        }
    }

    override suspend fun getById(id: Long): DataResult<TaskItem> {
        val item = taskItemDao.getById(id)?.toTaskItem()
        return if (item == null) {
            DataResult.Error(UiText.StringResource(R.string.oops_something_went_wrong))
        } else {
            DataResult.Success(item)
        }
    }
}
