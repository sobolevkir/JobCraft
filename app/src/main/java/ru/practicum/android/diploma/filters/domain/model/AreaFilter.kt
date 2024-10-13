package ru.practicum.android.diploma.filters.domain.model

import ru.practicum.android.diploma.common.data.network.dto.AreaFilterDto

data class AreaFilter(
    val id: Int,
    val parentId: Int?,
    val name: String,
    val areas: List<AreaFilterDto>?
)
