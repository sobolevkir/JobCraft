package ru.practicum.android.diploma.filters.data.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.data.network.dto.AreaFilterDto
import ru.practicum.android.diploma.common.data.network.dto.AreaSearchResponse
import ru.practicum.android.diploma.common.data.network.dto.FilterSearchRequest
import ru.practicum.android.diploma.common.data.network.dto.ResultCode
import ru.practicum.android.diploma.common.domain.model.ErrorType
import ru.practicum.android.diploma.common.util.Resource
import ru.practicum.android.diploma.filters.domain.AreaRepository
import ru.practicum.android.diploma.filters.domain.model.AreaFilter

class AreaRepositoryImpl(
    private val networkClient: NetworkClient,
    private val ioDispatcher: CoroutineDispatcher,
) : AreaRepository {
    override fun getAreas(): Flow<Resource<List<AreaFilter>>> = flow {
        val response = networkClient.doRequest(FilterSearchRequest.AREAS)
        when (response.resultCode) {
            ResultCode.SUCCESS -> {
                val areaSearchResponse = response as AreaSearchResponse
                val areaFilterDto = areaSearchResponse.items
                if (areaFilterDto.isEmpty()) {
                    emit(Resource.Error(ErrorType.NOTHING_FOUND))
                } else {
                    emit(Resource.Success(convertArea(areaSearchResponse.items)))
                }
            }
        }
    }.flowOn(ioDispatcher)

    fun convertArea(areaList: List<AreaFilterDto>): List<AreaFilter> {
        return areaList.map {
            AreaFilter(it.id, it.parentId, it.name, it.areas)
        }
    }
}
