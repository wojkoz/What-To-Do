package com.example.whattodo.data.mappers.task

import com.example.whattodo.data.local.entities.todos.TaskListEntity
import com.example.whattodo.data.model.task.CreateTaskList
import com.example.whattodo.domain.models.task.list.TaskList

fun TaskList.toTaskListEntity(): TaskListEntity {
    return TaskListEntity(
        id = this.id,
        title = this.title,
        isActive = this.isActive,
    )
}

fun TaskListEntity.toTaskList(
    doneTasksCount: Int,
    allTasksCount: Int,
    todoTasksCount: Int,
): TaskList {
    return TaskList(
        id = this.id,
        title = this.title,
        doneTasksCount = doneTasksCount,
        allTasksCount = allTasksCount,
        todoTasksCount = todoTasksCount,
        isActive = this.isActive,
    )
}

fun CreateTaskList.toTaskListEntity(): TaskListEntity {
    return TaskListEntity(
        title = this.title,
        isActive = this.isActive,
    )
}