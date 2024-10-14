package ru.practicum.android.diploma.filters.data.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.data.network.dto.FilterSearchRequest
import ru.practicum.android.diploma.common.data.network.dto.IndustryDto
import ru.practicum.android.diploma.common.data.network.dto.IndustrySearchResponse
import ru.practicum.android.diploma.common.data.network.dto.ResultCode
import ru.practicum.android.diploma.common.domain.model.ErrorType
import ru.practicum.android.diploma.common.util.Resource
import ru.practicum.android.diploma.filters.domain.IndustryRepository
import ru.practicum.android.diploma.filters.domain.model.Industry

class IndustryRepositoryImpl(
    private val networkClient: NetworkClient,
    private val ioDispatcher: CoroutineDispatcher
) : IndustryRepository {
    override fun getIndustries(): Flow<Resource<List<Industry>>> = flow {
        val response = networkClient.doRequest(FilterSearchRequest.INDUSTRIES)
        when (response.resultCode) {
            ResultCode.SUCCESS -> {
                val industrySearchResponse = response as IndustrySearchResponse
                val industryDto = industrySearchResponse.items
                if (industryDto.isEmpty()) {
                    emit(Resource.Error(ErrorType.NOTHING_FOUND))
                } else {
                    emit(Resource.Success(convertIndustry(industrySearchResponse.items)))
                }
            }
        }
    }.flowOn(ioDispatcher)

    fun convertIndustry(industryList: List<IndustryDto>): List<Industry> {
        return industryList.map {
            Industry(it.id, it.name)
        }
    }
}
