package com.example.whattodo.domain.usecase.task.list

import com.example.whattodo.domain.models.task.list.TaskList
import com.example.whattodo.domain.repository.todos.TasksListRepository

class GetActiveTaskListUseCase(
    private val tasksListRepository: TasksListRepository,
) {
    suspend operator fun invoke(): TaskList? {
        return tasksListRepository.getActive()
    }
}