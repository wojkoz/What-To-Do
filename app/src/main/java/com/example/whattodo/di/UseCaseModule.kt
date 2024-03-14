package com.example.whattodo.di

import com.example.whattodo.domain.repository.tasks.TaskItemRepository
import com.example.whattodo.domain.repository.tasks.TasksListRepository
import com.example.whattodo.domain.usecase.task.TaskItemUseCases
import com.example.whattodo.domain.usecase.task.TaskListUseCases
import com.example.whattodo.domain.usecase.task.item.CreateTaskItemUseCase
import com.example.whattodo.domain.usecase.task.item.GetByParentIdTaskItemUseCase
import com.example.whattodo.domain.usecase.task.item.GetTaskItemByIdUseCase
import com.example.whattodo.domain.usecase.task.item.InsertTaskItemUseCase
import com.example.whattodo.domain.usecase.task.item.SortTaskItemsUseCase
import com.example.whattodo.domain.usecase.task.item.TaskDoneUseCase
import com.example.whattodo.domain.usecase.task.item.TaskUnDoneUseCase
import com.example.whattodo.domain.usecase.task.item.ValidateLengthUseCase
import com.example.whattodo.domain.usecase.task.list.CreateTaskListUseCase
import com.example.whattodo.domain.usecase.task.list.DeleteTaskListUseCase
import com.example.whattodo.domain.usecase.task.list.GetActiveTaskListUseCase
import com.example.whattodo.domain.usecase.task.list.GetAllTaskListUseCase
import com.example.whattodo.domain.usecase.task.list.InsertTaskListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideTaskListUseCases(tasksListRepository: TasksListRepository): TaskListUseCases {
        return TaskListUseCases(
            createTaskListUseCase = CreateTaskListUseCase(tasksListRepository),
            getAllTaskListUseCase = GetAllTaskListUseCase(tasksListRepository),
            deleteTaskListUseCase = DeleteTaskListUseCase(tasksListRepository),
            getActiveTaskListUseCase = GetActiveTaskListUseCase(tasksListRepository),
            insertActiveTaskUseCase = InsertTaskListUseCase(tasksListRepository),
        )
    }

    @Provides
    fun provideTaskItemUseCases(taskItemRepository: TaskItemRepository): TaskItemUseCases {
        return TaskItemUseCases(
            createTaskItemUseCase = CreateTaskItemUseCase(taskItemRepository),
            getByParentIdTaskItemUseCase = GetByParentIdTaskItemUseCase(taskItemRepository),
            insertTaskItemUseCase = InsertTaskItemUseCase(taskItemRepository),
            validateLengthUseCase = ValidateLengthUseCase(),
            getTaskItemByIdUseCase = GetTaskItemByIdUseCase(taskItemRepository),
            taskDoneUseCase = TaskDoneUseCase(taskItemRepository),
            taskUnDoneUseCase = TaskUnDoneUseCase(taskItemRepository),
            sortTaskItemsUseCase = SortTaskItemsUseCase(),
        )
    }
}