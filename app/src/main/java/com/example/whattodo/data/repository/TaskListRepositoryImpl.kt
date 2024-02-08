package com.example.whattodo.data.repository

import com.example.whattodo.data.local.entities.todos.TaskItemDao
import com.example.whattodo.data.local.entities.todos.TaskListDao
import com.example.whattodo.data.local.entities.todos.TaskListEntity
import com.example.whattodo.data.mappers.task.toTaskList
import com.example.whattodo.data.mappers.task.toTaskListEntity
import com.example.whattodo.domain.models.TaskList
import com.example.whattodo.domain.repository.DataResult
import com.example.whattodo.domain.repository.todos.TasksListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TaskListRepositoryImpl(
    private val taskListDao: TaskListDao,
    private val taskItemDao: TaskItemDao,
) : TasksListRepository {
    override suspend fun getAll(): Flow<DataResult<List<TaskList>>> {
        return flow {
            emit(DataResult.Loading)
            val lists = taskListDao.getAll().map {
                val tasks = taskItemDao.getByParentId(it.id)
                val todoTasksCount = tasks.count { task -> !task.isDone }
                val doneTasksCount = tasks.size - todoTasksCount
                it.toTaskList(todoTasksCount = todoTasksCount, allTasksCount = tasks.size, doneTasksCount = doneTasksCount)
            }
            emit(DataResult.Success(lists))
        }
    }

    override suspend fun getActive(): Flow<DataResult<TaskListEntity?>> {
        return flow {
            emit(DataResult.Loading)
            val activeList = taskListDao.getActive()
            emit(DataResult.Success(activeList))
        }
    }

    override suspend fun insert(taskList: TaskList) {
        taskListDao.insert(taskList.toTaskListEntity())
    }

    override suspend fun delete(taskList: TaskList) {
        taskListDao.delete(taskList.toTaskListEntity())
    }
}
