package com.example.whattodo.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.whattodo.data.local.entities.todos.TaskItemDao
import com.example.whattodo.data.local.entities.todos.TaskItemEntity
import com.example.whattodo.data.local.entities.todos.TaskListDao
import com.example.whattodo.data.local.entities.todos.TaskListEntity

@Database(entities = [TaskListEntity::class, TaskItemEntity::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun taskListDao(): TaskListDao
    abstract fun taskItemDao(): TaskItemDao
}
