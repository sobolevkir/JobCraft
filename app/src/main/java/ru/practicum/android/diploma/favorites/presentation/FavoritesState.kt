package ru.practicum.android.diploma.favorites.presentation

import ru.practicum.android.diploma.common.domain.model.VacancyFromList

sealed interface FavoritesState {
    data object Empty : FavoritesState

    data object Error : FavoritesState
    data class Content(
        val tracks: List<VacancyFromList>
    ) : FavoritesState

}
