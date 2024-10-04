package ru.practicum.android.diploma.search.domain.model

data class Vacancy(
    val vacancyName: String,
    val salary: String?,
    val region: String,
    val employerName: String,
    val employerLogoUrl: String?,
    val experience: String?,
    val schedule: String?,
    val description: String?,
    val keySkills: String?
)
