package ru.practicum.android.diploma.search.presentation

import ru.practicum.android.diploma.common.domain.model.VacancyFromList

sealed interface SearchState {

    data object InternetError : SearchState
    data object ServerError : SearchState
    data object NothingFound : SearchState
    data object Loading : SearchState
    data object Updating : SearchState
    data object Default : SearchState
    data object  UpdatingError: SearchState

    data class SearchResult(
        val vacancies: List<VacancyFromList>,
        val found: Int
    ) : SearchState
}
