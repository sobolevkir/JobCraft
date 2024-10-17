package ru.practicum.android.diploma.filters.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.domain.model.ErrorType
import ru.practicum.android.diploma.filters.domain.model.Area

interface AreaInteractor {
    fun getRegions(): Flow<Pair<List<Area>?, ErrorType?>>
    fun getCountries(): Flow<Pair<List<Area>?, ErrorType?>>

    fun getOtherRegions(): Flow<Pair<List<Area>?, ErrorType?>>
}
