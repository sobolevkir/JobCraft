package ru.practicum.android.diploma.search.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.util.Resource
import ru.practicum.android.diploma.search.domain.model.VacanciesSearchResult

interface VacanciesRepository {

    fun searchVacancies(queryText: String, page: Int): Flow<Resource<VacanciesSearchResult>>

}
