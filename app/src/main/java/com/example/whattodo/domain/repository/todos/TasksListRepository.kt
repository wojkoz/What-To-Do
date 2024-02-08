package com.example.whattodo.domain.repository.todos

import com.example.whattodo.data.local.entities.todos.TaskListEntity
import com.example.whattodo.domain.models.TaskList
import com.example.whattodo.domain.repository.DataResult
import kotlinx.coroutines.flow.Flow

interface TasksListRepository {
    suspend fun getAll(): Flow<DataResult<List<TaskList>>>

    suspend fun getActive(): Flow<DataResult<TaskListEntity?>>

    suspend fun insert(taskList: TaskList)

    suspend fun delete(taskList: TaskList)
}