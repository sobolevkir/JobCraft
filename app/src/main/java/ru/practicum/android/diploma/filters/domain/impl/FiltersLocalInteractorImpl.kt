package ru.practicum.android.diploma.filters.domain.impl

import ru.practicum.android.diploma.filters.domain.FiltersLocalInteractor
import ru.practicum.android.diploma.filters.domain.FiltersLocalRepository
import ru.practicum.android.diploma.filters.domain.model.FilterParameters

class FiltersLocalInteractorImpl(
    private val filtersRepository: FiltersLocalRepository
) : FiltersLocalInteractor {

    override fun saveFilters(filterParameters: FilterParameters) {
        filtersRepository.saveFilterParameters(filterParameters)
    }

    override fun getFilters(): FilterParameters? {
        return filtersRepository.getFilterParameters()
    }

    override fun clearFilters() {
        filtersRepository.clearFilters()
    }

}
