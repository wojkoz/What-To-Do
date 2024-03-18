package com.example.whattodo.di

import android.content.Context
import androidx.room.Room
import com.example.whattodo.data.local.db.Database
import com.example.whattodo.data.local.entities.tasks.TaskItemDao
import com.example.whattodo.data.local.entities.tasks.TaskListDao
import com.example.whattodo.data.repository.TaskItemRepositoryImpl
import com.example.whattodo.data.repository.TaskListRepositoryImpl
import com.example.whattodo.domain.repository.tasks.TaskItemRepository
import com.example.whattodo.domain.repository.tasks.TasksListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): Database {
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

    @Singleton
    @Provides
    fun provideTaskItemRepository(taskItemDao: TaskItemDao): TaskItemRepository = TaskItemRepositoryImpl(taskItemDao)

    @Singleton
    @Provides
    fun provideTaskListRepository(
        taskListDao: TaskListDao,
        taskItemDao: TaskItemDao,
    ): TasksListRepository {
        return TaskListRepositoryImpl(taskListDao, taskItemDao)
    }
}

