package com.example.whattodo.domain.usecase.task.list

import com.example.whattodo.data.model.task.CreateTaskList
import com.example.whattodo.domain.repository.tasks.TasksListRepository

class CreateTaskListUseCase(
    private val tasksListRepository: TasksListRepository,
) {
    suspend operator fun invoke(
        setActive: Boolean,
        title: String,
    ) {
        if (setActive) {
            tasksListRepository.getActive()?.let {
                tasksListRepository.insert(it.copy(isActive = false))
            }
        }
        val createTaskList = CreateTaskList(isActive = setActive, title = title)
        tasksListRepository.insert(createTaskList)
    }
}