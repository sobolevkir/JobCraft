package ru.practicum.android.diploma.search.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.vacancy.util.ResourceDetails
import ru.practicum.android.diploma.search.domain.model.VacanciesSearchResult

interface VacanciesRepository {

    fun searchVacancies(options: Map<String, String>): Flow<ResourceDetails<VacanciesSearchResult>>

}
