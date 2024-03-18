package com.example.whattodo.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.whattodo.data.local.entities.tasks.TaskItemDao
import com.example.whattodo.data.local.entities.tasks.TaskItemEntity
import com.example.whattodo.data.local.entities.tasks.TaskListDao
import com.example.whattodo.data.local.entities.tasks.TaskListEntity

@Database(entities = [TaskListEntity::class, TaskItemEntity::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun taskListDao(): TaskListDao
    abstract fun taskItemDao(): TaskItemDao
}
