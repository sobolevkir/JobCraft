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
import ru.practicum.android.diploma.filters.domain.model.Area

class AreaRepositoryImpl(
    private val networkClient: NetworkClient,
    private val ioDispatcher: CoroutineDispatcher,
) : AreaRepository {
    override fun getAreas(): Flow<Resource<List<Area>>> = flow {
        val response = networkClient.doRequest(FilterSearchRequest.AREAS)
        when (response.resultCode) {
            ResultCode.SUCCESS -> {
                val areaSearchResponse = response as AreaSearchResponse
                val areaFilterDto = areaSearchResponse.items
                if (areaFilterDto.isEmpty()) {
                    emit(Resource.Error(ErrorType.NOTHING_FOUND))
                } else {
                    emit(Resource.Success(getAllNestedAreas(areaFilterDto)))
                }
            }
        }
    }.flowOn(ioDispatcher)

    fun convertToAreas(areaDtoList: List<AreaFilterDto>?): List<Area> {
        return if (areaDtoList != null) {
            areaDtoList.map {
                Area(it.id, it.parentId, it.name)
            }
        } else emptyList()
    }

    fun getAllNestedAreas(areaDtoList: List<AreaFilterDto>, parentId: String? = null): List<Area> {
        val areaList = mutableListOf<Area>()
        areaDtoList.forEach { areaDto ->
            areaList.add(Area(areaDto.id, parentId, areaDto.name))
            areaDto.areas?.let { nestedAreas ->
            areaList.addAll(getAllNestedAreas(nestedAreas, areaDto.id))
        }
    }
    return areaList
}
}
