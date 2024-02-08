package com.example.whattodo.domain.usecase.task.list

import com.example.whattodo.domain.models.task.list.TaskList
import com.example.whattodo.domain.repository.todos.TasksListRepository

class DeleteTaskListUseCase(
    private val tasksListRepository: TasksListRepository,
) {
    suspend operator fun invoke(taskList: TaskList) {
        return tasksListRepository.delete(taskList)
    }
}