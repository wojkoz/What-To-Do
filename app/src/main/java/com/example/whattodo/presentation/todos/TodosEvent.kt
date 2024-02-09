package com.example.whattodo.presentation.todos

import com.example.whattodo.domain.models.task.item.TaskItem
import com.example.whattodo.domain.models.task.list.TaskList

sealed class TodosEvent {
    data class OnTaskListSelect(val taskList: TaskList) : TodosEvent()
    data class OnTaskDone(val taskItem: TaskItem) : TodosEvent()
}