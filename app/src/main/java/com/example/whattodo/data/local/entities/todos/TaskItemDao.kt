package com.example.whattodo.data.local.entities.todos

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
}