package ru.practicum.android.diploma.search.data.network

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.common.ext.isNetworkConnected
import ru.practicum.android.diploma.search.data.dto.Response
import ru.practicum.android.diploma.search.data.dto.ResultCode.RESULT_CODE_BAD_REQUEST
import ru.practicum.android.diploma.search.data.dto.ResultCode.RESULT_CODE_NO_INTERNET
import ru.practicum.android.diploma.search.data.dto.ResultCode.RESULT_CODE_SERVER_ERROR
import ru.practicum.android.diploma.search.data.dto.ResultCode.RESULT_CODE_SUCCESS
import ru.practicum.android.diploma.search.data.dto.VacancyRequest

private const val LOG_RESULT_CODE = "ResultCode"

class RetrofitNetworkClient(
    private val api: HHApi,
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher
) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        val result = when {
            !context.isNetworkConnected -> Response().apply { resultCode = RESULT_CODE_NO_INTERNET }
            dto !is VacancyRequest -> Response().apply { resultCode = RESULT_CODE_BAD_REQUEST }
            else -> {
                withContext(ioDispatcher) {
                    try {
                        val response = api.getVacancy(dto.vacancyId.toString())
                        response.apply { resultCode = RESULT_CODE_SUCCESS }
                    } catch (e: HttpException) {
                        Response().apply {
                            resultCode = RESULT_CODE_SERVER_ERROR
                            Log.e(LOG_RESULT_CODE, "VacancyResponse - resultCode: $resultCode", e)
                        }
                    }
                }
            }
        }
        return result
    }
}
