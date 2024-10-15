package ru.practicum.android.diploma.common.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.filters.domain.model.Area
import ru.practicum.android.diploma.filters.domain.model.FilterParameters
import ru.practicum.android.diploma.filters.domain.model.Industry

class FilterParametersViewModel : ViewModel() {

    private var filtersAppliedLiveEvent = SingleLiveEvent<Boolean>()
    fun getFiltersAppliedLiveEvent(): SingleLiveEvent<Boolean> = filtersAppliedLiveEvent

    private var filterParametersLiveData = MutableLiveData<FilterParameters>()
    fun getFilterParametersLiveData(): LiveData<FilterParameters> = filterParametersLiveData

    fun applyFilters() {
        filtersAppliedLiveEvent.value = true
    }

    fun setFilterParametersLiveData(parameters: FilterParameters) {
        filterParametersLiveData.postValue(parameters)
    }

    fun setRegion(region: Area?) {
        with(filterParametersLiveData) {
            this.postValue(
                this.value?.copy(region = region)
            )
        }
    }

    fun setCountry(country: Area?) {
        with(filterParametersLiveData) {
            this.postValue(
                this.value?.copy(country = country)
            )
        }
    }

    fun setIndustry(industry: Industry?) {
        with(filterParametersLiveData) {
            this.postValue(
                this.value?.copy(industry = industry)
            )
        }
    }

    fun setExpectedSalary(salary: Int?) {
        with(filterParametersLiveData) {
            this.postValue(
                this.value?.copy(expectedSalary = salary)
            )
        }
    }

    fun setOnlyWithSalary(onlyWithSalary: Boolean) {
        with(filterParametersLiveData) {
            this.postValue(
                this.value?.copy(onlyWithSalary = onlyWithSalary)
            )
        }
    }

}
