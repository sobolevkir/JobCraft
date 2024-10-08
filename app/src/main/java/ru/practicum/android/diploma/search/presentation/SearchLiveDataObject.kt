package ru.practicum.android.diploma.search.presentation

import ru.practicum.android.diploma.common.domain.model.VacancyFromList

data class SearchLiveDataObject(
    val vacancies: List<VacancyFromList>,
    val code: Int,
    val count: Int,
    val isSearch: Boolean
)
