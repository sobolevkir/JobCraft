package ru.practicum.android.diploma.search.data.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.common.domain.model.ErrorType
import ru.practicum.android.diploma.common.util.Resource
import ru.practicum.android.diploma.search.data.mapper.VacancyMapper.toDomain
import ru.practicum.android.diploma.search.data.network.NetworkClient
import ru.practicum.android.diploma.search.data.network.dto.ResultCode
import ru.practicum.android.diploma.search.data.network.dto.VacanciesSearchRequest
import ru.practicum.android.diploma.search.data.network.dto.VacanciesSearchResponse
import ru.practicum.android.diploma.search.domain.VacanciesRepository
import ru.practicum.android.diploma.search.domain.model.VacanciesSearchResult

class VacanciesRepositoryImpl(
    private val networkClient: NetworkClient,
    private val ioDispatcher: CoroutineDispatcher
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
                        items = vacanciesDto.map { it.toDomain() },
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
            else -> emit(Resource.Error(ErrorType.UNKNOWN_ERROR))
        }

    }.flowOn(ioDispatcher)

}
