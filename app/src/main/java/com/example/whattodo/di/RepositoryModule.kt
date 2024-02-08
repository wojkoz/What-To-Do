package com.example.whattodo.di

import android.content.Context
import androidx.room.Room
import com.example.whattodo.data.local.db.Database
import com.example.whattodo.data.local.entities.todos.TaskItemDao
import com.example.whattodo.data.local.entities.todos.TaskListDao
import com.example.whattodo.data.repository.TaskItemRepositoryImpl
import com.example.whattodo.data.repository.TaskListRepositoryImpl
import com.example.whattodo.domain.repository.todos.TaskItemRepository
import com.example.whattodo.domain.repository.todos.TasksListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton

@InstallIn(Singleton::class)
@Module
object RepositoryModule {

    @Provides
    fun provideDatabase(context: Context): Database {
        return Room.databaseBuilder(
            context = context,
            name = "wtd-db",
            klass = Database::class.java
        ).build()
    }

    @Provides
    fun provideTaskListDao(db: Database): TaskListDao = db.taskListDao()

    @Provides
    fun provideTaskItemDao(db: Database): TaskItemDao = db.taskItemDao()

    @Provides
    fun provideTaskItemRepository(taskItemDao: TaskItemDao): TaskItemRepository = TaskItemRepositoryImpl(taskItemDao)

    @Provides
    fun provideTaskListRepository(
        taskListDao: TaskListDao,
        taskItemDao: TaskItemDao,
    ): TasksListRepository {
        return TaskListRepositoryImpl(taskListDao, taskItemDao)
    }
}

