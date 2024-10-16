package ru.practicum.android.diploma.filters.domain

import ru.practicum.android.diploma.filters.domain.model.FilterParameters

interface FiltersLocalInteractor {

    fun saveFilters(filterParameters: FilterParameters)

    fun getFilters(): FilterParameters?

    fun clearFilters()

}
