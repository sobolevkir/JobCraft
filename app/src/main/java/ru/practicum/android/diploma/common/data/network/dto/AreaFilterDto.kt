package ru.practicum.android.diploma.common.data.network.dto

data class AreaFilterDto(
    val id: Int,
    val parentId: Int?,
    val name:String,
    val areas: List<AreaFilterDto>?
)
