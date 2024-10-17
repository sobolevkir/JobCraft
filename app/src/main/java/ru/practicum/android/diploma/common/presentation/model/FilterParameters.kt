package ru.practicum.android.diploma.common.presentation.model

import ru.practicum.android.diploma.filters.domain.model.Area
import ru.practicum.android.diploma.filters.domain.model.Industry

data class FilterParameters(
    val country: Area?,
    val region: Area?,
    val industry: Industry?,
    val expectedSalary: Int?,
    val onlyWithSalary: Boolean = false
)
