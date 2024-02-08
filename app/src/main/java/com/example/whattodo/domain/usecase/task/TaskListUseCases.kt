package com.example.whattodo.domain.usecase.task

import com.example.whattodo.domain.usecase.task.list.CreateTaskListUseCase
import com.example.whattodo.domain.usecase.task.list.DeleteTaskListUseCase
import com.example.whattodo.domain.usecase.task.list.GetActiveTaskListUseCase
import com.example.whattodo.domain.usecase.task.list.GetAllTaskListUseCase
import com.example.whattodo.domain.usecase.task.list.InsertTaskListUseCase

data class TaskListUseCases(
    val createTaskListUseCase: CreateTaskListUseCase,
    val deleteTaskListUseCase: DeleteTaskListUseCase,
    val getActiveTaskListUseCase: GetActiveTaskListUseCase,
    val getAllTaskListUseCase: GetAllTaskListUseCase,
    val insertActiveTaskUseCase: InsertTaskListUseCase
)
