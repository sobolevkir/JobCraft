package ru.practicum.android.diploma.search.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.model.VacancyListItem

interface SearchInteractor{
    fun doSearch(request: String): Flow<List<VacancyListItem>>
}
