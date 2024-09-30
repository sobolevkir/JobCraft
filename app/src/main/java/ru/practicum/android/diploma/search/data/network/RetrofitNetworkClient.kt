package ru.practicum.android.diploma.search.data.network

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.common.ext.isNetworkConnected
import ru.practicum.android.diploma.search.NetworkClient
import ru.practicum.android.diploma.search.data.dto.RESULT_CODE_BAD_REQUEST
import ru.practicum.android.diploma.search.data.dto.RESULT_CODE_NO_INTERNET
import ru.practicum.android.diploma.search.data.dto.RESULT_CODE_SERVER_ERROR
import ru.practicum.android.diploma.search.data.dto.RESULT_CODE_SUCCESS
import ru.practicum.android.diploma.search.data.dto.Response
import ru.practicum.android.diploma.search.data.dto.VacancyRequest


class RetrofitNetworkClient(
    private val api: HHApi,
    private val context: Context
) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        if (!context.isNetworkConnected) {
            return Response().apply { resultCode = RESULT_CODE_NO_INTERNET }
        }

        if (dto !is VacancyRequest) {
            return Response().apply { resultCode = RESULT_CODE_BAD_REQUEST }
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = api.getVacancy(dto.id.toString())
                response.apply { resultCode = RESULT_CODE_SUCCESS }
            } catch (e: Throwable) {
                Response().apply { resultCode = RESULT_CODE_SERVER_ERROR }
            }
        }
    }
}


