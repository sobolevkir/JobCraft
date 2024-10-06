package ru.practicum.android.diploma.common.data.network.dto

data class VacanciesSearchResponse(
    val items: List<VacancyFromListDto>,
    val found: Int,
    val page: Int,
    val pages: Int
) : Response()
