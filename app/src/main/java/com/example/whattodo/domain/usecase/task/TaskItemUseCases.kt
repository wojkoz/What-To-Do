package com.example.whattodo.domain.usecase.task

import com.example.whattodo.domain.usecase.task.item.CreateTaskItemUseCase
import com.example.whattodo.domain.usecase.task.item.GetByParentIdTaskItemUseCase
import com.example.whattodo.domain.usecase.task.item.GetTaskItemByIdUseCase
import com.example.whattodo.domain.usecase.task.item.InsertTaskItemUseCase
import com.example.whattodo.domain.usecase.task.item.SortTaskItemsUseCase
import com.example.whattodo.domain.usecase.task.item.TaskDoneUseCase
import com.example.whattodo.domain.usecase.task.item.TaskUnDoneUseCase
import com.example.whattodo.domain.usecase.task.item.ValidateLengthUseCase

data class TaskItemUseCases(
    val createTaskItemUseCase: CreateTaskItemUseCase,
    val getByParentIdTaskItemUseCase: GetByParentIdTaskItemUseCase,
    val insertTaskItemUseCase: InsertTaskItemUseCase,
    val validateLengthUseCase: ValidateLengthUseCase,
    val getTaskItemByIdUseCase: GetTaskItemByIdUseCase,
    val taskDoneUseCase: TaskDoneUseCase,
    val taskUnDoneUseCase: TaskUnDoneUseCase,
    val sortTaskItemsUseCase: SortTaskItemsUseCase,
)
