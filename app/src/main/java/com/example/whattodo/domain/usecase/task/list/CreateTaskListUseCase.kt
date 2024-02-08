package com.example.whattodo.domain.usecase.task.list

import com.example.whattodo.data.model.task.CreateTaskList
import com.example.whattodo.domain.repository.todos.TasksListRepository

class CreateTaskListUseCase(
    private val tasksListRepository: TasksListRepository,
) {
    suspend operator fun invoke(
        isActive: Boolean,
        title: String,
    ) {
        val createTaskList = CreateTaskList(isActive = isActive, title = title)
        tasksListRepository.insert(createTaskList)
    }
}