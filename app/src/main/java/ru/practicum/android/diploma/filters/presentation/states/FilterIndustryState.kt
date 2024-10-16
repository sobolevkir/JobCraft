package ru.practicum.android.diploma.filters.presentation.states

import ru.practicum.android.diploma.filters.domain.model.Industry

sealed interface FilterIndustryState {
    data object NothingFound : FilterIndustryState
    data object UnknownError : FilterIndustryState
    data object InternetError : FilterIndustryState
    data object Loading : FilterIndustryState

    data class IndustryFound(val industries: List<Industry>) : FilterIndustryState
}
