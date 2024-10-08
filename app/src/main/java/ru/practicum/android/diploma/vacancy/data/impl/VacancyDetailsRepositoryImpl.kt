package ru.practicum.android.diploma.vacancy.data.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.data.network.dto.ResultCode
import ru.practicum.android.diploma.common.data.network.dto.VacancyDetailsRequest
import ru.practicum.android.diploma.common.data.network.dto.VacancyDetailsResponse
import ru.practicum.android.diploma.common.domain.model.ErrorType
import ru.practicum.android.diploma.common.domain.model.VacancyDetails
import ru.practicum.android.diploma.common.util.Resource
import ru.practicum.android.diploma.vacancy.data.converter.VacancyDetailsConverter
import ru.practicum.android.diploma.vacancy.domain.VacancyDetailsRepository

class VacancyDetailsRepositoryImpl(
    private val networkClient: NetworkClient,
    private val ioDispatcher: CoroutineDispatcher
) : VacancyDetailsRepository {

    override fun getVacancyDetails(vacancyId: Long): Flow<Resource<VacancyDetails>> = flow {
        val response = networkClient.doRequest(VacancyDetailsRequest(vacancyId))
        when (response.resultCode) {
            ResultCode.SUCCESS -> {
                val vacancyDetailsResponse = response as VacancyDetailsResponse
                val resultData = VacancyDetailsConverter.convert(vacancyDetailsResponse)
                emit(Resource.Success(resultData))
            }

            ResultCode.CONNECTION_PROBLEM -> emit(Resource.Error(ErrorType.CONNECTION_PROBLEM))
            ResultCode.BAD_REQUEST -> emit(Resource.Error(ErrorType.BAD_REQUEST))
            ResultCode.NOTHING_FOUND -> emit(Resource.Error(ErrorType.NOTHING_FOUND))
            ResultCode.SERVER_ERROR -> emit(Resource.Error(ErrorType.SERVER_ERROR))
            ResultCode.FORBIDDEN_ERROR -> emit(Resource.Error(ErrorType.FORBIDDEN_ERROR))
            else -> emit(Resource.Error(ErrorType.UNKNOWN_ERROR))
        }

    }.flowOn(ioDispatcher)

}
