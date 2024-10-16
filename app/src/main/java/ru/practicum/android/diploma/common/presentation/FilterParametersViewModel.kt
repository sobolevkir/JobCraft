package ru.practicum.android.diploma.common.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.practicum.android.diploma.filters.domain.FiltersLocalInteractor
import ru.practicum.android.diploma.filters.domain.model.Area
import ru.practicum.android.diploma.filters.domain.model.FilterParameters
import ru.practicum.android.diploma.filters.domain.model.Industry

class FilterParametersViewModel : ViewModel(), KoinComponent {

    private val filtersLocalInteractor: FiltersLocalInteractor by inject()

    private var filtersAppliedLiveEvent = SingleLiveEvent<Boolean>()
    fun getFiltersAppliedLiveEvent(): SingleLiveEvent<Boolean> = filtersAppliedLiveEvent

    private var filterParametersLiveData = MutableLiveData<FilterParameters>()
    fun getFilterParametersLiveData(): LiveData<FilterParameters> = filterParametersLiveData

    init {
        getFiltersFromLocalStorage()
    }

    private fun getFiltersFromLocalStorage() {
        filterParametersLiveData.postValue(
            filtersLocalInteractor.getFilters() ?: FilterParameters(
                null,
                null,
                null,
                null
            )
        )
    }

    fun applyFilters() {
        filtersAppliedLiveEvent.value = true
    }

    fun clearFilters() {
        filterParametersLiveData.postValue(FilterParameters(null, null, null, null))
        saveFiltersToLocalStorage()
    }

    fun setRegion(region: Area?) {
        with(filterParametersLiveData) {
            this.postValue(
                this.value?.copy(region = region)
            )
        }
        saveFiltersToLocalStorage()
    }

    fun setCountry(country: Area?) {
        with(filterParametersLiveData) {
            this.postValue(
                this.value?.copy(country = country)
            )
        }
        saveFiltersToLocalStorage()
    }

    fun setIndustry(industry: Industry?) {
        with(filterParametersLiveData) {
            this.postValue(
                this.value?.copy(industry = industry)
            )
        }
        saveFiltersToLocalStorage()
    }

    fun setExpectedSalary(salary: Int?) {
        with(filterParametersLiveData) {
            this.postValue(
                this.value?.copy(expectedSalary = salary)
            )
        }
        saveFiltersToLocalStorage()
    }

    fun setOnlyWithSalary(onlyWithSalary: Boolean) {
        with(filterParametersLiveData) {
            this.postValue(
                this.value?.copy(onlyWithSalary = onlyWithSalary)
            )
        }
        saveFiltersToLocalStorage()
    }

    private fun saveFiltersToLocalStorage() {
        filtersLocalInteractor.saveFilters(
            filterParametersLiveData.value ?: FilterParameters(null, null, null, null)
        )
    }

}
