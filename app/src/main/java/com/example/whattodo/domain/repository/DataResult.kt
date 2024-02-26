package com.example.whattodo.domain.repository

import com.example.whattodo.utils.UiText

sealed class DataResult<out T> {
    data class Success<T>(val data: T?) : DataResult<T>()
    data class Error(val message: UiText) : DataResult<Nothing>()
    data object Loading : DataResult<Nothing>()
}