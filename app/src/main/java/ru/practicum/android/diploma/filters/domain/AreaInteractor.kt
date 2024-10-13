package ru.practicum.android.diploma.filters.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.domain.model.ErrorType
import ru.practicum.android.diploma.filters.domain.model.AreaFilter

interface AreaInteractor {
    fun getAreas(): Flow<Pair<List<AreaFilter>?, ErrorType?>>
}
