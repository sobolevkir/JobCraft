package ru.practicum.android.diploma.filters.presentation.states

import ru.practicum.android.diploma.filters.domain.model.Area
import ru.practicum.android.diploma.filters.domain.model.Industry

data class FiltersState(val country: String,
                        val region: String,
                        val industry: String,
                        val isOnlyWithSalary: Boolean,
                        val minSalary: String,
    val isEmpty: Boolean)
