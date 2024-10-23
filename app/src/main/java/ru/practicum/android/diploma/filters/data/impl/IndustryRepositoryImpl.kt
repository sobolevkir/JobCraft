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
                emit(Resource.Success(convertIndustry(industrySearchResponse.items)))
            }

            ResultCode.CONNECTION_PROBLEM -> {
                emit(Resource.Error(ErrorType.CONNECTION_PROBLEM))
            }

            ResultCode.NOTHING_FOUND -> {
                emit(Resource.Error(ErrorType.NOTHING_FOUND))
            }

            ResultCode.BAD_REQUEST -> emit(Resource.Error(ErrorType.BAD_REQUEST))
            ResultCode.SERVER_ERROR -> emit(Resource.Error(ErrorType.SERVER_ERROR))
            ResultCode.FORBIDDEN_ERROR -> emit(Resource.Error(ErrorType.FORBIDDEN_ERROR))
            else -> emit(Resource.Error(ErrorType.UNKNOWN_ERROR))
        }
    }.flowOn(ioDispatcher)

    private fun convertIndustry(industryList: List<IndustryDto>): List<Industry> {
        return industryList.map {
            Industry(it.id, it.name)
        }
    }
}
