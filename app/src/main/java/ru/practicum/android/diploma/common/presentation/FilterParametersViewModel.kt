package ru.practicum.android.diploma.common.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.filters.domain.FiltersLocalInteractor
import ru.practicum.android.diploma.filters.domain.impl.FiltersLocalInteractorImpl
import ru.practicum.android.diploma.filters.domain.model.Area
import ru.practicum.android.diploma.filters.domain.model.FilterParameters
import ru.practicum.android.diploma.filters.domain.model.Industry

class FilterParametersViewModel(private val interactor: FiltersLocalInteractor) : ViewModel() {

    private var parameters = interactor.getFilters()

    private var filtersAppliedLiveEvent = SingleLiveEvent<Boolean>()
    fun getFiltersAppliedLiveEvent(): SingleLiveEvent<Boolean> = filtersAppliedLiveEvent

    private var filterParametersLiveData = MutableLiveData<FilterParameters>()
    fun getFilterParametersLiveData(): LiveData<FilterParameters> = filterParametersLiveData

    fun applyFilters() {
        filtersAppliedLiveEvent.value = true
        interactor.saveFilters(parameters!!)
    }

    fun getSavedParameters(){
        interactor.getFilters().let {
            filterParametersLiveData.postValue(it!!)
        }
    }

    fun setFilterParametersLiveData(parameters: FilterParameters) {
        this.parameters = parameters
        filterParametersLiveData.postValue(parameters)
    }

    fun setRegion(region: Area?) {
        with(filterParametersLiveData) {
            parameters = this.value?.copy(region = region)
            this.postValue(
                parameters
            )
        }
    }

    fun setCountry(country: Area?) {
        with(filterParametersLiveData) {
            parameters = this.value?.copy(country = country)
            this.postValue(
                parameters
            )
        }
    }

    fun setIndustry(industry: Industry?) {
        with(filterParametersLiveData) {
            parameters = this.value?.copy(industry = industry)
            this.postValue(
                parameters
            )
        }
    }

    fun setExpectedSalary(salary: Int?) {
        with(filterParametersLiveData) {
            parameters = this.value?.copy(expectedSalary = salary)
            this.postValue(
                parameters
            )
        }
    }

    fun setOnlyWithSalary(onlyWithSalary: Boolean) {
        with(filterParametersLiveData) {
            parameters = this.value?.copy(onlyWithSalary = onlyWithSalary)
            this.postValue(
                parameters
            )
        }
    }

}
