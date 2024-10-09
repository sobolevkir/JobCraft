package ru.practicum.android.diploma.search.data.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.common.data.converter.ParametersConverter
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.data.network.dto.ResultCode
import ru.practicum.android.diploma.common.data.network.dto.VacanciesSearchRequest
import ru.practicum.android.diploma.common.data.network.dto.VacanciesSearchResponse
import ru.practicum.android.diploma.common.data.network.dto.VacancyFromListDto
import ru.practicum.android.diploma.common.domain.model.ErrorType
import ru.practicum.android.diploma.common.domain.model.VacancyFromList
import ru.practicum.android.diploma.vacancy.util.ResourceDetails
import ru.practicum.android.diploma.search.domain.VacanciesRepository
import ru.practicum.android.diploma.search.domain.model.VacanciesSearchResult

class VacanciesRepositoryImpl(
    private val networkClient: NetworkClient,
    private val ioDispatcher: CoroutineDispatcher,
    private val parametersConverter: ParametersConverter
) : VacanciesRepository {

    override fun searchVacancies(options: Map<String, String>): Flow<ResourceDetails<VacanciesSearchResult>> = flow {
        val response = networkClient.doRequest(VacanciesSearchRequest(options))
        when (response.resultCode) {
            ResultCode.SUCCESS -> {
                val vacanciesSearchResponse = response as VacanciesSearchResponse
                val vacanciesDto = vacanciesSearchResponse.items
                if (vacanciesDto.isEmpty()) {
                    emit(ResourceDetails.Error(ErrorType.NOTHING_FOUND))
                } else {
                    val searchResultData = VacanciesSearchResult(
                        items = vacanciesDto.convertToVacancyFromList(),
                        found = vacanciesSearchResponse.found,
                        page = vacanciesSearchResponse.page,
                        pages = vacanciesSearchResponse.pages
                    )
                    emit(ResourceDetails.Success(searchResultData))
                }
            }

            ResultCode.CONNECTION_PROBLEM -> emit(ResourceDetails.Error(ErrorType.CONNECTION_PROBLEM))
            ResultCode.BAD_REQUEST -> emit(ResourceDetails.Error(ErrorType.BAD_REQUEST))
            ResultCode.NOTHING_FOUND -> emit(ResourceDetails.Error(ErrorType.NOTHING_FOUND))
            ResultCode.SERVER_ERROR -> emit(ResourceDetails.Error(ErrorType.SERVER_ERROR))
            ResultCode.FORBIDDEN_ERROR -> emit(ResourceDetails.Error(ErrorType.FORBIDDEN_ERROR))
            else -> emit(ResourceDetails.Error(ErrorType.UNKNOWN_ERROR))
        }

    }.flowOn(ioDispatcher)

    private fun List<VacancyFromListDto>.convertToVacancyFromList(): List<VacancyFromList> {
        return this.map {
            VacancyFromList(
                id = it.id.toLongOrNull() ?: -1L,
                name = it.name,
                salary = it.salary?.let { salaryDto -> parametersConverter.convert(salaryDto) },
                areaName = it.area.name,
                employerName = it.employer.name,
                employerLogoUrl240 = it.employer.logoUrls?.logoUrl240
            )
        }
    }

}
