package ru.practicum.android.diploma.common.data.network

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.common.data.network.dto.Response
import ru.practicum.android.diploma.common.data.network.dto.ResultCode
import ru.practicum.android.diploma.common.data.network.dto.VacanciesSearchRequest
import ru.practicum.android.diploma.common.ext.isNetworkConnected

class RetrofitNetworkClient(
    private val api: HHApi,
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        val response = Response()

        if (!context.isNetworkConnected) {
            response.resultCode = ResultCode.CONNECTION_PROBLEM
        } else if (dto !is VacanciesSearchRequest) {
            response.resultCode = ResultCode.BAD_REQUEST
        } else {
            return withContext(ioDispatcher) {
                try {
                    val apiResponse = api.searchVacancies(dto.options)
                    apiResponse.apply { resultCode = ResultCode.SUCCESS }
                } catch (ex: HttpException) {
                    response.resultCode = when (ex.code()) {
                        ResultCode.NOTHING_FOUND,
                        ResultCode.SERVER_ERROR,
                        ResultCode.FORBIDDEN_ERROR -> ex.code()
                        else -> ResultCode.UNKNOWN_ERROR
                    }
                    response
                }
            }
        }

        return response
    }
}
