package ru.practicum.android.diploma.search.presentation

import ru.practicum.android.diploma.common.domain.model.VacancyFromList

sealed interface SearchState {

    data object InternetError : SearchState
    data object ServerError : SearchState
    data object NothingFound : SearchState
    data object Loading : SearchState
    data object Default : SearchState
    data class NewSearchResult(
        val vacancies: List<VacancyFromList>,
        val found: Int
    ) : SearchState

    // Paging
    data object NextPageError : SearchState
    data object NextPageLoading : SearchState
    data class NextPageSearchResult(
        val vacancies: List<VacancyFromList>,
        val found: Int
    ) : SearchState

}
