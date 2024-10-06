package ru.practicum.android.diploma.search.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.domain.model.ErrorType
import ru.practicum.android.diploma.search.domain.model.VacanciesSearchResult

interface VacanciesInteractor {
    fun searchVacancies(queryText: String, page: Int): Flow<Pair<VacanciesSearchResult?, ErrorType?>>
}
