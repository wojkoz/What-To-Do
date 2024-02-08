package com.example.whattodo.domain.usecase.task.list

import com.example.whattodo.domain.models.task.list.TaskList
import com.example.whattodo.domain.repository.DataResult
import com.example.whattodo.domain.repository.todos.TasksListRepository
import kotlinx.coroutines.flow.Flow

class GetAllTaskListUseCase(
    private val tasksListRepository: TasksListRepository,
) {
    suspend operator fun invoke(): Flow<DataResult<List<TaskList>>> {
        return tasksListRepository.getAll()
    }
}