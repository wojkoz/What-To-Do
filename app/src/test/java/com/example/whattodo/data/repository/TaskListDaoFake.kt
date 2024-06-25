package com.example.whattodo.data.repository

import com.example.whattodo.data.local.entities.tasks.TaskListDao
import com.example.whattodo.data.local.entities.tasks.TaskListEntity

class TaskListDaoFake : TaskListDao {

    private val db: MutableSet<TaskListEntity> = mutableSetOf()

    override suspend fun getAll(): List<TaskListEntity> {
        return db.toList()
    }

    override suspend fun getActive(): TaskListEntity? {
        return db.firstOrNull { it.isActive }
    }

    override suspend fun insert(taskList: TaskListEntity) {
        val taskListWithId = if (db.any { it.id == taskList.id }) {
            db.remove(taskList)
            taskList.copy(id = 1)
        } else {
            taskList.copy(id = (db.lastOrNull()?.id ?: 0) + 1)
        }
        db.add(taskListWithId)
    }

    override suspend fun delete(taskList: TaskListEntity) {
        db.remove(taskList)
    }

    override suspend fun clearDb() {
        db.clear()
    }

    override suspend fun contains(name: String): TaskListEntity? {
        return db.firstOrNull { it.title == name }
    }
}
