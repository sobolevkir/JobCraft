package ru.practicum.android.diploma.search.data.network.dto

class ResultCode {
    companion object {
        const val UNKNOWN_ERROR = -1
        const val CONNECTION_PROBLEM = 0
        const val NOTHING_FOUND = 404
        const val FORBIDDEN_ERROR = 403
        const val BAD_REQUEST = 400
        const val SUCCESS = 200
        const val SERVER_ERROR = 500
    }
}
