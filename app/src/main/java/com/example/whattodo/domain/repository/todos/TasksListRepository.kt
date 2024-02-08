package com.example.whattodo.domain.repository.todos

import com.example.whattodo.data.model.task.CreateTaskList
import com.example.whattodo.domain.models.task.list.TaskList
import com.example.whattodo.domain.repository.DataResult
import kotlinx.coroutines.flow.Flow

interface TasksListRepository {
    suspend fun getAll(): Flow<DataResult<List<TaskList>>>

    suspend fun getActive(): Flow<DataResult<TaskList?>>

    suspend fun insert(taskList: TaskList)

    suspend fun insert(taskList: CreateTaskList)

    suspend fun delete(taskList: TaskList)
}