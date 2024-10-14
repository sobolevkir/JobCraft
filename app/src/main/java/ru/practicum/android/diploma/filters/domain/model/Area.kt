package ru.practicum.android.diploma.filters.domain.model

import java.io.Serializable

class Area(
    val parentId: String? = null,
    val id: String,
    val name: String
): Serializable
