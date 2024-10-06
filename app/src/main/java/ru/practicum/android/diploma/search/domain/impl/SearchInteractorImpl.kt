package ru.practicum.android.diploma.search.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.SearchInteractor
import ru.practicum.android.diploma.search.domain.SearchRepository
import ru.practicum.android.diploma.search.domain.model.VacancyListItem

class SearchInteractorImpl(private val repository : SearchRepository) : SearchInteractor {
    override fun doSearch(request: String): Flow<List<VacancyListItem>>{
        return repository.doSearch(request)
    }
}
