package ru.practicum.android.diploma.filters.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.util.Resource
import ru.practicum.android.diploma.filters.domain.model.Area

interface AreaRepository {
    fun getAreas(): Flow<Resource<List<Area>>>
}
