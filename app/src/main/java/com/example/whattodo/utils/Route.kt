package com.example.whattodo.utils

object Route {
    const val MAIN_SCREEN = "main-screen"

    const val parentListIdParameter = "{parent-list-id}"
    const val taskIdParameter = "{task-id}"
    const val TODOS_CREATE_SCREEN = "todos-create-screen/$parentListIdParameter/$taskIdParameter"

    fun passArgToTodosCreateScreen(
        parentListId: Long,
        taskId: Long?,
    ): String {
        return TODOS_CREATE_SCREEN
            .replace(parentListIdParameter, parentListId.toString())
            .replace(taskIdParameter, (taskId ?: -1).toString())
    }

    fun normalizeParameter(param: String): String {
        return param
            .replace("{", "")
            .replace("}", "")
    }
}