package ru.practicum.android.diploma.common.util

import ru.practicum.android.diploma.common.domain.model.ErrorType

sealed class Resource<T>(val data: T? = null, val errorType: ErrorType? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(errorType: ErrorType) : Resource<T>(null, errorType)
}
