package ru.practicum.android.diploma.filters.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.domain.model.ErrorType
import ru.practicum.android.diploma.filters.domain.model.Area

interface AreaInteractor {
    fun getAreas(): Flow<Pair<List<Area>?, ErrorType?>>
}
