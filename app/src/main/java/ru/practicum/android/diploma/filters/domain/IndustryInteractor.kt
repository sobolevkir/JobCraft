package ru.practicum.android.diploma.filters.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.domain.model.ErrorType
import ru.practicum.android.diploma.filters.domain.model.Industry

interface IndustryInteractor {
    fun getIndustries(): Flow<Pair<List<Industry>?, ErrorType?>>
}
