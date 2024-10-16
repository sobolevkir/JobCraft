package ru.practicum.android.diploma.common.data.network.dto

data class AreaFilterDto(
    val id: String,
    val parentId: String?,
    val name: String,
    val areas: List<AreaFilterDto>?
)
