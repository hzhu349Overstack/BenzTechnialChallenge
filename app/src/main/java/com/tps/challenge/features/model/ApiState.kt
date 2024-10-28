package com.tps.challenge.features.model

sealed class ApiState<out T> {
    object Empty : ApiState<Nothing>()
    object Loading : ApiState<Nothing>()
    data class Success<out T>(val data: T) : ApiState<T>()
    data class Error(val message: String, val exception: Throwable? = null) : ApiState<Nothing>()
}
