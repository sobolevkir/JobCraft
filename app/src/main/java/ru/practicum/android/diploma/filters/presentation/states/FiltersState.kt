package ru.practicum.android.diploma.filters.presentation.states

data class FiltersState(
    val country: String,
    val region: String,
    val industry: String,
    val isOnlyWithSalary: Boolean,
    val minSalary: String,
    val isEmpty: Boolean
)
