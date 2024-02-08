package com.example.whattodo.domain.repository

sealed class DataResult<out T> {
    data class Success<T>(val data: T?) : DataResult<T>()
    data class Error(val message: String) : DataResult<Nothing>()
    data object Loading : DataResult<Nothing>()
}