package com.example.whattodo.domain.usecase.task.item

import com.example.whattodo.utils.UiText

data class UseCaseResult<T>(
    val isSuccess: Boolean,
    val errorMessage: UiText?,
    val data: T?,
)