package com.example.whattodo.domain.usecase.task.list

import com.example.whattodo.domain.models.task.list.TaskList
import com.example.whattodo.domain.repository.tasks.TasksListRepository

class ImportAllTasksUseCase(
    private val tasksListRepository: TasksListRepository,
) {
    suspend operator fun invoke(tasksList: List<TaskList>, clearDb: Boolean) {
        tasksListRepository.importAll(tasksLists = tasksList, clearDb = clearDb)
    }
}