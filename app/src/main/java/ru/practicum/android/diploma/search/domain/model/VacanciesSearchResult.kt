package ru.practicum.android.diploma.search.domain.model

import ru.practicum.android.diploma.common.domain.model.VacancyFromList

data class VacanciesSearchResult (
    val items: List<VacancyFromList>,
    val found: Int,
    val page: Int,
    val pages: Int
)
