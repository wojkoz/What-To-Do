package com.example.whattodo.data.repository

import com.example.whattodo.data.local.entities.tasks.TaskItemDao
import com.example.whattodo.data.local.entities.tasks.TaskItemEntity

class TaskItemDaoFake : TaskItemDao {

    private val db: MutableSet<TaskItemEntity> = mutableSetOf()

    override suspend fun insert(item: TaskItemEntity): Long {
        val itemWithId = if (db.contains(item)) {
            db.remove(item)
            item
        } else {
            item.copy(id = (db.lastOrNull()?.id ?: 0) + 1)
        }

        db.add(itemWithId)
        return itemWithId.id
    }

    override suspend fun delete(item: TaskItemEntity) {
        db.remove(item)
    }

    override suspend fun getByParentId(parentId: Long): List<TaskItemEntity> {
        return if (db.isEmpty()) {
            emptyList()
        } else {
            db.groupBy { it.parentListId }.getValue(parentId)
        }
    }

    override suspend fun getById(id: Long): TaskItemEntity? {
        return db.firstOrNull { it.id == id }
    }

    override suspend fun clearDb() {
        db.clear()
    }

    override suspend fun upsertAll(items: List<TaskItemEntity>): List<Long> {
        return items.map { insert(it) }
    }
}
