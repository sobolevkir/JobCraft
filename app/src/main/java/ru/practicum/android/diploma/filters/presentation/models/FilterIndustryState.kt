package ru.practicum.android.diploma.filters.presentation.models

import ru.practicum.android.diploma.filters.ui.IndustryForUi

sealed interface FilterIndustryState {
    data object NothingFound : FilterIndustryState
    data object UnknownError : FilterIndustryState
    data object InternetError : FilterIndustryState
    data object Loading : FilterIndustryState
    data object NoList : FilterIndustryState

    data class IndustryFound(val industries: List<IndustryForUi>) : FilterIndustryState
}
