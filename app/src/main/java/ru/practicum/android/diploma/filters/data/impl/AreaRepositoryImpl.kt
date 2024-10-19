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

private const val OTHERS_PARENT_ID = "1001"

class AreaRepositoryImpl(
    private val networkClient: NetworkClient,
    private val ioDispatcher: CoroutineDispatcher,
) : AreaRepository {
    override fun getRegions(): Flow<Resource<List<Area>>> = flow {
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

            ResultCode.CONNECTION_PROBLEM -> emit(Resource.Error(ErrorType.CONNECTION_PROBLEM))
            ResultCode.BAD_REQUEST -> emit(Resource.Error(ErrorType.BAD_REQUEST))
            ResultCode.NOTHING_FOUND -> emit(Resource.Error(ErrorType.NOTHING_FOUND))
            ResultCode.SERVER_ERROR -> emit(Resource.Error(ErrorType.SERVER_ERROR))
            ResultCode.FORBIDDEN_ERROR -> emit(Resource.Error(ErrorType.FORBIDDEN_ERROR))
        }
    }.flowOn(ioDispatcher)

    override fun getOtherRegions(): Flow<Resource<List<Area>>> = flow {
        val response = networkClient.doRequest(FilterSearchRequest.AREAS)
        when (response.resultCode) {
            ResultCode.SUCCESS -> {
                val areaSearchResponse = response as AreaSearchResponse
                val areaFilterDto = areaSearchResponse.items
                if (areaFilterDto.isEmpty()) {
                    emit(Resource.Error(ErrorType.NOTHING_FOUND))
                } else {
                    emit(Resource.Success(getOtherCountriesFromAreas(areaFilterDto)))
                }
            }

            ResultCode.CONNECTION_PROBLEM -> emit(Resource.Error(ErrorType.CONNECTION_PROBLEM))
            ResultCode.BAD_REQUEST -> emit(Resource.Error(ErrorType.BAD_REQUEST))
            ResultCode.NOTHING_FOUND -> emit(Resource.Error(ErrorType.NOTHING_FOUND))
            ResultCode.SERVER_ERROR -> emit(Resource.Error(ErrorType.SERVER_ERROR))
            ResultCode.FORBIDDEN_ERROR -> emit(Resource.Error(ErrorType.FORBIDDEN_ERROR))
        }
    }.flowOn(ioDispatcher)

    override fun getCountries(): Flow<Resource<List<Area>>> = flow {
        val response = networkClient.doRequest(FilterSearchRequest.AREAS)
        when (response.resultCode) {
            ResultCode.SUCCESS -> {
                val areaSearchResponse = response as AreaSearchResponse
                val areaFilterDto = areaSearchResponse.items
                if (areaFilterDto.isEmpty()) {
                    emit(Resource.Error(ErrorType.NOTHING_FOUND))
                } else {
                    emit(Resource.Success(getCountriesFromAreas(areaFilterDto)))
                }
            }

            ResultCode.CONNECTION_PROBLEM -> emit(Resource.Error(ErrorType.CONNECTION_PROBLEM))
            ResultCode.BAD_REQUEST -> emit(Resource.Error(ErrorType.BAD_REQUEST))
            ResultCode.NOTHING_FOUND -> emit(Resource.Error(ErrorType.NOTHING_FOUND))
            ResultCode.SERVER_ERROR -> emit(Resource.Error(ErrorType.SERVER_ERROR))
            ResultCode.FORBIDDEN_ERROR -> emit(Resource.Error(ErrorType.FORBIDDEN_ERROR))
        }
    }.flowOn(ioDispatcher)

    private fun getCountriesFromAreas(areaDtoList: List<AreaFilterDto>): List<Area> {
        val areaList = mutableListOf<Area>()
        areaDtoList.forEach {
            areaList.add(Area(it.parentId, it.id, it.name))
        }
        return areaList
    }

    private fun getAllNestedAreas(areaDtoList: List<AreaFilterDto>, parentId: String? = null): List<Area> {
        val areaList = mutableListOf<Area>()
        areaDtoList.forEach { areaDto ->
            areaList.add(Area(parentId ?: OTHERS_PARENT_ID, areaDto.id, areaDto.name))
            areaDto.areas?.let { nestedAreas ->
                areaList.addAll(getAllNestedAreas(nestedAreas, parentId ?: areaDto.id))
            }
        }

        return areaList
    }

    private fun getOtherCountriesFromAreas(areaDtoList: List<AreaFilterDto>): List<Area> {
        val areaList = mutableListOf<Area>()

        areaDtoList.forEach { areaDto ->
            if (areaDto.parentId == OTHERS_PARENT_ID || areaDto.id == OTHERS_PARENT_ID) {
                areaList.add(Area(OTHERS_PARENT_ID, areaDto.id, areaDto.name))
                areaDto.areas?.let { nestedAreas ->
                    areaList.addAll(getAllNestedAreas(nestedAreas, areaDto.id))
                }
            }
        }

        return areaList
    }

}
