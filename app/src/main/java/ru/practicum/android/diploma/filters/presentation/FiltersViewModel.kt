package ru.practicum.android.diploma.filters.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.filters.domain.FiltersLocalInteractor
import ru.practicum.android.diploma.common.presentation.model.FilterParameters

class FiltersViewModel(private val interactor: FiltersLocalInteractor) : ViewModel() {
    private val stateLiveData = MutableLiveData<FilterParameters>()

    init {
        stateLiveData.postValue(
            interactor.getFilters() ?: FilterParameters(null, null, null, null, false)
        )
    }

    fun getStateLiveData(): LiveData<FilterParameters> = stateLiveData

    fun saveFiltersToLocalStorage(filters: FilterParameters?) {
        filters?.let {
            interactor.saveFilters(filters)
        }
    }
}
