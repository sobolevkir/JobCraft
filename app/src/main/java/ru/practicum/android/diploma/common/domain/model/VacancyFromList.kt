package ru.practicum.android.diploma.common.domain.model

data class VacancyFromList(
    val id: Long,
    val name: String,
    val salary: String?,
    val areaName: String,
    val employerName: String,
    val employerLogoUrl240: String?,
)
