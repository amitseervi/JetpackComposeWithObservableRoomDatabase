package com.amit.radiuscompose.model.response

sealed interface RepositoryStatus<T> {

    class Loading<T> : RepositoryStatus<T>

    data class Success<T>(val data: T) : RepositoryStatus<T>

    data class Error<T>(
        val message: String,
        val code: Int
    ) : RepositoryStatus<T>
}