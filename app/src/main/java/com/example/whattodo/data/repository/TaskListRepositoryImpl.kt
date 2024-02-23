package com.example.whattodo.data.repository

import com.example.whattodo.data.local.entities.todos.TaskItemDao
import com.example.whattodo.data.local.entities.todos.TaskListDao
import com.example.whattodo.data.local.entities.todos.TaskListEntity
import com.example.whattodo.data.mappers.task.toTaskItem
import com.example.whattodo.data.mappers.task.toTaskList
import com.example.whattodo.data.mappers.task.toTaskListEntity
import com.example.whattodo.data.model.task.CreateTaskList
import com.example.whattodo.domain.models.task.list.TaskList
import com.example.whattodo.domain.repository.DataResult
import com.example.whattodo.domain.repository.todos.TasksListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TaskListRepositoryImpl @Inject constructor(
    private val taskListDao: TaskListDao,
    private val taskItemDao: TaskItemDao,
) : TasksListRepository {
    override suspend fun getAll(): Flow<DataResult<List<TaskList>>> {
        return flow {
            emit(DataResult.Loading)
            val lists = taskListDao.getAll().mapNotNull {
                populateTaskList(it)
            }
            emit(DataResult.Success(lists))
        }
    }

    override suspend fun getActive(): TaskList? {
        return populateTaskList(taskListDao.getActive())
    }

    override suspend fun insert(taskList: TaskList) {
        taskListDao.insert(taskList.toTaskListEntity())
    }

    override suspend fun insert(taskList: CreateTaskList) {
        taskListDao.insert(taskList.toTaskListEntity())
    }

    override suspend fun delete(taskList: TaskList) {
        taskListDao.delete(taskList.toTaskListEntity())
    }

    private suspend fun populateTaskList(taskListEntity: TaskListEntity?): TaskList? {
        if (taskListEntity?.id == null) {
            return null
        }

        val tasks = taskItemDao.getByParentId(taskListEntity.id)
        val todoTasks = tasks.filter { task -> !task.isDone }
        val doneTasks = tasks.filter {task -> task.isDone}
        return taskListEntity.toTaskList(
            todoTasksCount = todoTasks.size,
            allTasksCount = tasks.size,
            doneTasksCount = doneTasks.size,
            doneTaskItems = doneTasks.map { it.toTaskItem() },
            todoTaskItems = todoTasks.map { it.toTaskItem() }
        )
    }
}
