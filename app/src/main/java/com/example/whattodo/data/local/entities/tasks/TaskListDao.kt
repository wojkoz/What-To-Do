package com.example.whattodo.data.local.entities.tasks

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface TaskListDao {

    @Query("SELECT * FROM TaskListEntity")
    suspend fun getAll(): List<TaskListEntity>

    @Query("SELECT * FROM TaskListEntity WHERE isActive")
    suspend fun getActive(): TaskListEntity?

    @Upsert
    suspend fun insert(taskList: TaskListEntity)

    @Delete
    suspend fun delete(taskList: TaskListEntity)

    @Query("DELETE FROM TaskListEntity")
    suspend fun clearDb()

    @Query("SELECT * FROM TaskListEntity WHERE title LIKE :name LIMIT 1")
    suspend fun contains(name: String): TaskListEntity?
}