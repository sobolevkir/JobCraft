package ru.practicum.android.diploma.filters.domain

import ru.practicum.android.diploma.common.presentation.model.FilterParameters

interface FiltersLocalRepository {

    fun saveFilterParameters(filterParameters: FilterParameters)

    fun getFilterParameters(): FilterParameters?

    fun clearFilters()

}
