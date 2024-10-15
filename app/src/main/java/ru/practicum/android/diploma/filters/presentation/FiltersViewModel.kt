package ru.practicum.android.diploma.filters.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.filters.domain.FiltersLocalInteractor
import ru.practicum.android.diploma.filters.presentation.states.FiltersState

class FiltersViewModel(private val interactor: FiltersLocalInteractor) : ViewModel() {
    private val stateLiveData = MutableLiveData<FiltersState>()

    fun getStateLiveData(): LiveData<FiltersState> = stateLiveData

    fun getFilters() {
        val states = interactor.getFilters()
        var country = ""
        var region = ""
        var industry = ""
        var isOnlyWithSalary = false
        var minSalary = ""
        var isEmpty = true

        if (states != null) {
            isEmpty = false
            if (states.country != null) {
                country = states.country.name
            }
            if (states.region != null) {
                region = states.region.name
            }
            if (states.industry != null) {
                industry = states.industry.name
            }
            if (states.expectedSalary != null) {
                minSalary = states.expectedSalary.toString()
            }
            isOnlyWithSalary = states.onlyWithSalary
        }

        stateLiveData.postValue(FiltersState(country, region, industry, isOnlyWithSalary, minSalary, isEmpty))
    }
}
