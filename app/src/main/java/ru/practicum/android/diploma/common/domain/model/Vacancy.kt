package ru.practicum.android.diploma.common.domain.model

data class Vacancy(
    val id: Long,
    val vacancyName: String,
    val salary: Salary?,
    val region: String,
    val employerName: String,
    val employerLogoUrl: String?,
    val experience: String?,
    val schedule: String?,
    val description: String?,
    val keySkills: List<String>?,
    val isFavorite: Boolean = false,
)
