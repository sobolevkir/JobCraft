package ru.practicum.android.diploma.filters.domain.impl

import ru.practicum.android.diploma.filters.domain.FiltersLocalInteractor
import ru.practicum.android.diploma.filters.domain.FiltersLocalRepository
import ru.practicum.android.diploma.filters.domain.model.FilterParameters

class FiltersLocalInteractorImpl(
    private val repository: FiltersLocalRepository
) : FiltersLocalInteractor {

    override fun saveFilters(filterParameters: FilterParameters) {
        repository.saveFilterParameters(filterParameters)
    }

    override fun getFilters(): FilterParameters? {
        return repository.getFilterParameters()
    }

    override fun clearFilters() {
        repository.clearFilters()
    }

}
