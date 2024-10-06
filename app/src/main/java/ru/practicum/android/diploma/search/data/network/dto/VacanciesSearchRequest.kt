package ru.practicum.android.diploma.search.data.network.dto

data class VacanciesSearchRequest(
    val queryText: String,
    val page: Int
)
