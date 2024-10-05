package ru.practicum.android.diploma.common.domain.model

data class VacancyDetails(
    val id: Long,
    val name: String,
    val salary: Salary?,
    val areaName: String,
    val employerName: String,
    val employerLogoUrl240: String?,
    val contacts: Contacts?,
    val experience: String?,
    val scheduleName: String,
    val description: String,
    val keySkills: List<String>,
    val address: Address?,
    val isFavorite: Boolean = false,
)
