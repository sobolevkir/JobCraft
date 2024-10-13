package ru.practicum.android.diploma.filters.domain.model

data class FilterParameters(
    val country: FilterParameter?,
    val region: FilterParameter?,
    val industry: FilterParameter?,
    val expectedSalary: Int?,
    val onlyWithSalary: Boolean = false
)

data class FilterParameter(
    val id: String,
    val name: String
)
