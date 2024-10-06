package ru.practicum.android.diploma.search.domain.model

data class VacancyListItem(
    val vacancyName: String,
    val salary: String?,
    val region: String,
    val employerLogoUrl: String?,
)
