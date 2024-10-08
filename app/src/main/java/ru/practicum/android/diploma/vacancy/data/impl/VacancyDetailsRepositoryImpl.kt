package ru.practicum.android.diploma.vacancy.data.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.common.data.formatter.ParametersConverter
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.data.network.dto.ResultCode
import ru.practicum.android.diploma.common.data.network.dto.VacancyDetailsRequest
import ru.practicum.android.diploma.common.data.network.dto.VacancyDetailsResponse
import ru.practicum.android.diploma.common.domain.model.ErrorType
import ru.practicum.android.diploma.common.domain.model.VacancyDetails
import ru.practicum.android.diploma.common.util.Resource
import ru.practicum.android.diploma.vacancy.domain.VacancyDetailsRepository

class VacancyDetailsRepositoryImpl(
    private val networkClient: NetworkClient,
    private val ioDispatcher: CoroutineDispatcher,
    private val parametersConverter: ParametersConverter
) : VacancyDetailsRepository {

    override fun getVacancyDetails(vacancyId: Long): Flow<Resource<VacancyDetails>> = flow {
        val response = networkClient.doRequest(VacancyDetailsRequest(vacancyId))
        when (response.resultCode) {
            ResultCode.SUCCESS -> {
                val vacancyDetailsResponse = response as VacancyDetailsResponse
                val resultData = vacancyDetailsResponse.convertToVacancyDetails()
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

    private fun VacancyDetailsResponse.convertToVacancyDetails(): VacancyDetails {
        return with(this) {
            VacancyDetails(
                id = id.toLongOrNull() ?: -1L,
                name = name,
                salary = salary?.let { salaryDto -> parametersConverter.convert(salaryDto) },
                areaName = area.name,
                employerName = employer?.name,
                employerLogoUrl240 = employer?.logoUrls?.logoUrl240,
                experience = experience?.name,
                scheduleName = schedule?.name,
                description = description,
                keySkills = keySkills.map { it.name },
                address = address?.let { addressDto -> parametersConverter.convert(addressDto) },
                alternateUrl = alternateUrl
            )
        }
    }

}
