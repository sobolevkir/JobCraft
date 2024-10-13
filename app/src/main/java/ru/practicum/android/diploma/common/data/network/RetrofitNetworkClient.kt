package ru.practicum.android.diploma.common.data.network

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.common.data.network.dto.AreaSearchResponse
import ru.practicum.android.diploma.common.data.network.dto.FilterSearchRequest
import ru.practicum.android.diploma.common.data.network.dto.IndustrySearchResponse
import ru.practicum.android.diploma.common.data.network.dto.Response
import ru.practicum.android.diploma.common.data.network.dto.ResultCode
import ru.practicum.android.diploma.common.data.network.dto.VacanciesSearchRequest
import ru.practicum.android.diploma.common.data.network.dto.VacancyDetailsRequest
import ru.practicum.android.diploma.common.ext.isNetworkConnected

class RetrofitNetworkClient(
    private val api: HHApi,
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher,
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        val response = Response()

        if (!context.isNetworkConnected) {
            response.resultCode = ResultCode.CONNECTION_PROBLEM
        } else if (dto !is VacanciesSearchRequest && dto !is VacancyDetailsRequest && dto !is FilterSearchRequest) {
            response.resultCode = ResultCode.BAD_REQUEST
        } else {
            return withContext(ioDispatcher) {
                try {
                    val apiResponse =
                        when (dto) {
                            is VacanciesSearchRequest -> api.searchVacancies(dto.options)
                            is FilterSearchRequest -> handleFilterType(dto)
                            else -> {
                                api.getVacancyDetails((dto as VacancyDetailsRequest).vacancyId.toString())
                            }
                        }
                    apiResponse.apply { resultCode = ResultCode.SUCCESS }
                } catch (ex: HttpException) {
                    response.resultCode = handleHttpException(ex)
                    response
                }
            }
        }

        return response
    }

    private suspend fun handleFilterType(dto: Any): Response {
        return withContext(ioDispatcher) {
            when (dto) {
                FilterSearchRequest.INDUSTRIES -> IndustrySearchResponse(api.getIndustries())
                FilterSearchRequest.AREAS -> AreaSearchResponse(
                    api.getAreas()
                ) as Response /*здесь должен быть метод аналогичный getIndustries() только для странl,l,l,l;km';*/
                else -> IndustrySearchResponse(
                    api.getIndustries()
                ) as Response/*десь должен быть метод аналогичный getIndustries() только для региона*/
            }
        }
    }

    private fun handleHttpException(ex: HttpException): Int {
        return when (ex.code()) {
            ResultCode.NOTHING_FOUND,
            ResultCode.SERVER_ERROR,
            ResultCode.FORBIDDEN_ERROR,
            -> ex.code()

            else -> ResultCode.UNKNOWN_ERROR
        }
    }
}
