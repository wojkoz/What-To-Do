package com.example.whattodo.data.local.entities.tasks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface TaskItemDao {
    @Upsert
    suspend fun insert(item: TaskItemEntity)

    @Delete
    suspend fun delete(item: TaskItemEntity)

    @Query("SELECT * FROM TaskItemEntity WHERE parent_list_id = :parentId")
    suspend fun getByParentId(parentId: Long): List<TaskItemEntity>

    @Query("SELECT * FROm TaskItemEntity WHERE id = :id")
    suspend fun getById(id: Long): TaskItemEntity?

    @Query("DELETE FROM TaskItemEntity")
    suspend fun clearDb()

    @Upsert
    suspend fun upsertAll(items: List<TaskItemEntity>)
}