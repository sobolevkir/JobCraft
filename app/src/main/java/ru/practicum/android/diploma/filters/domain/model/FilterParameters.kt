package ru.practicum.android.diploma.filters.domain.model

data class FilterParameters(
    val country: Area?,
    val region: Area?,
    val industry: Industry?,
    val expectedSalary: Int?,
    val onlyWithSalary: Boolean = false
)
