package com.example.whattodo.domain.models

sealed class TaskPriority(val priorityAsInt: Int) {
    data object Low : TaskPriority(priorityAsInt = 0)
    data object High : TaskPriority(priorityAsInt = 1)

    companion object {
        fun fromInt(priority: Int): TaskPriority {
            return when (priority) {
                0 -> Low
                1 -> High
                else -> Low
            }
        }
    }
}
