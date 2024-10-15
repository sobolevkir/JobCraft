package ru.practicum.android.diploma.common.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.filters.domain.model.FilterParameters

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

}
