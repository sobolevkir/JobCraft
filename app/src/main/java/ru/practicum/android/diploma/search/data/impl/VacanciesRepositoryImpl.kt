package ru.practicum.android.diploma.search.data.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.common.data.formatter.ParametersConverter
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.data.network.dto.ResultCode
import ru.practicum.android.diploma.common.data.network.dto.VacanciesSearchRequest
import ru.practicum.android.diploma.common.data.network.dto.VacanciesSearchResponse
import ru.practicum.android.diploma.common.data.network.dto.VacancyFromListDto
import ru.practicum.android.diploma.common.domain.model.ErrorType
import ru.practicum.android.diploma.common.domain.model.VacancyFromList
import ru.practicum.android.diploma.common.util.Resource
import ru.practicum.android.diploma.search.domain.VacanciesRepository
import ru.practicum.android.diploma.search.domain.model.VacanciesSearchResult

class VacanciesRepositoryImpl(
    private val networkClient: NetworkClient,
    private val ioDispatcher: CoroutineDispatcher,
    private val parametersConverter: ParametersConverter
) : VacanciesRepository {

    override fun searchVacancies(options: Map<String, String>): Flow<Resource<VacanciesSearchResult>> = flow {
        val response = networkClient.doRequest(VacanciesSearchRequest(options))
        when (response.resultCode) {
            ResultCode.SUCCESS -> {
                val vacanciesSearchResponse = response as VacanciesSearchResponse
                val vacanciesDto = vacanciesSearchResponse.items
                if (vacanciesDto.isEmpty()) {
                    emit(Resource.Error(ErrorType.NOTHING_FOUND))
                } else {
                    val searchResultData = VacanciesSearchResult(
                        items = vacanciesDto.convertToVacancyFromList(),
                        found = vacanciesSearchResponse.found,
                        page = vacanciesSearchResponse.page,
                        pages = vacanciesSearchResponse.pages
                    )
                    emit(Resource.Success(searchResultData))
                }
            }

            ResultCode.CONNECTION_PROBLEM -> emit(Resource.Error(ErrorType.CONNECTION_PROBLEM))
            ResultCode.BAD_REQUEST -> emit(Resource.Error(ErrorType.BAD_REQUEST))
            ResultCode.NOTHING_FOUND -> emit(Resource.Error(ErrorType.NOTHING_FOUND))
            ResultCode.SERVER_ERROR -> emit(Resource.Error(ErrorType.SERVER_ERROR))
            ResultCode.FORBIDDEN_ERROR -> emit(Resource.Error(ErrorType.FORBIDDEN_ERROR))
            else -> emit(Resource.Error(ErrorType.UNKNOWN_ERROR))
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
