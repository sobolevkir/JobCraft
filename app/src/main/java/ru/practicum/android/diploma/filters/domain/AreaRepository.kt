package ru.practicum.android.diploma.filters.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.util.Resource
import ru.practicum.android.diploma.filters.domain.model.Area

interface AreaRepository {
    fun getRegions(): Flow<Resource<List<Area>>>

    fun getCountries(): Flow<Resource<List<Area>>>

    fun getOtherRegions(): Flow<Resource<List<Area>>>
}
