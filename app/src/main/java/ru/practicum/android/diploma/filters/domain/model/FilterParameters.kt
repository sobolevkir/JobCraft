package ru.practicum.android.diploma.filters.domain.model

data class FilterParameters(
    val country: Country?,
    val region: Region?,
    val industry: Industry?,
    val expectedSalary: Int?,
    val onlyWithSalary: Boolean = false
)
