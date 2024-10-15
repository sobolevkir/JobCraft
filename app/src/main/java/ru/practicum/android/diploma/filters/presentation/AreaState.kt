package ru.practicum.android.diploma.filters.presentation

import ru.practicum.android.diploma.filters.domain.model.Area

sealed interface AreaState {

    data object InternetError : AreaState
    data object ServerError : AreaState
    data object NothingFound : AreaState
    data object NoList : AreaState
    data object Loading : AreaState
    data object Updating : AreaState

    data class SearchResult(
        val regions: List<Area>,
        val found: Int
    ) : AreaState
}
