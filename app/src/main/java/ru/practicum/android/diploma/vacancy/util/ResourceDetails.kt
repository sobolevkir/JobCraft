package ru.practicum.android.diploma.vacancy.util

import ru.practicum.android.diploma.common.domain.model.ErrorType

sealed class ResourceDetails<T>(val data: T? = null, val errorType: ErrorType? = null) {
    class Success<T>(data: T): ResourceDetails<T>(data)
    class Error<T>(errorType: ErrorType): ResourceDetails<T>(null, errorType)
}
