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
        if (!context.isNetworkConnected) {
            return Response().apply {
                resultCode = ResultCode.CONNECTION_PROBLEM
            }
        }
        if (dto !is VacanciesSearchRequest) {
            return Response().apply {
                resultCode = ResultCode.BAD_REQUEST
            }
        }
        return withContext(ioDispatcher) {
            try {
                val response = api.searchVacancies(dto.options)
                /*val response = when(dto) {
                    is VacanciesSearchRequest -> api.searchVacancies(dto.options)
                    is VacancyDetailsRequest -> api.getVacancy(dto.id)
                }*/
                response.apply { resultCode = ResultCode.SUCCESS }
            } catch (ex: HttpException) {
                Response().apply {
                    resultCode = when (ex.code()) {
                        ResultCode.NOTHING_FOUND,
                        ResultCode.SERVER_ERROR,
                        ResultCode.FORBIDDEN_ERROR -> ex.code()

                        else -> ResultCode.UNKNOWN_ERROR
                    }
                }
            } catch (er: Error) {
                Response().apply {
                    resultCode = ResultCode.UNKNOWN_ERROR
                }
            }
        }
    }
}
