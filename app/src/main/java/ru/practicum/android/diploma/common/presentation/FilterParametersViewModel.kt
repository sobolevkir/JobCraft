package ru.practicum.android.diploma.common.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.practicum.android.diploma.common.presentation.model.FilterParameters
import ru.practicum.android.diploma.filters.domain.FiltersLocalInteractor
import ru.practicum.android.diploma.filters.domain.model.Area
import ru.practicum.android.diploma.filters.domain.model.Industry
import ru.practicum.android.diploma.filters.presentation.models.PlaceParameters

class FilterParametersViewModel : ViewModel(), KoinComponent {

    private val filtersLocalInteractor: FiltersLocalInteractor by inject()

    private var filtersAppliedLiveEvent = SingleLiveEvent<Boolean>()
    fun getFiltersAppliedLiveEvent(): SingleLiveEvent<Boolean> = filtersAppliedLiveEvent

    private var filtersChangedLiveData = MutableLiveData<Boolean>()
    fun getFiltersChangedLiveData(): LiveData<Boolean> = filtersChangedLiveData

    private var filterParametersLiveData = MutableLiveData<FilterParameters>()
    fun getFilterParametersLiveData(): LiveData<FilterParameters> = filterParametersLiveData

    private var placeTemporaryLiveData = MutableLiveData<PlaceParameters>()
    fun getPlaceTemporaryLiveData(): LiveData<PlaceParameters> = placeTemporaryLiveData

    private val emptyFilterParameters: FilterParameters = FilterParameters(null, null, null, null)

    init {
        getFiltersFromLocalStorage()
    }

    private fun getFiltersFromLocalStorage() {
        filterParametersLiveData.value = filtersLocalInteractor.getFilters() ?: emptyFilterParameters
        placeTemporaryLiveData.value = PlaceParameters(
            countryTemp = filterParametersLiveData.value?.country,
            regionTemp = filterParametersLiveData.value?.region
        )
    }

    private fun saveFiltersToLocalStorage() {
        val localFilterParameters = filtersLocalInteractor.getFilters()
        if (localFilterParameters != filterParametersLiveData.value) {
            Log.d("FILTERS!!!", "saveFiltersToLocalStorage()")
            filtersLocalInteractor.saveFilters(filterParametersLiveData.value ?: emptyFilterParameters)
            filtersChangedLiveData.value = true
        }
    }

    // Работа с PlaceTemporaryLiveData
    fun applyPlaceTemporaryLiveData() {
        setCountry(placeTemporaryLiveData.value?.countryTemp)
        setRegion(placeTemporaryLiveData.value?.regionTemp)
    }

    fun resetPlaceTemporaryLiveData() {
        placeTemporaryLiveData.value = PlaceParameters(
            countryTemp = filterParametersLiveData.value?.country,
            regionTemp = filterParametersLiveData.value?.region
        )
    }

    fun setCountryTemporary(country: Area?) {
        placeTemporaryLiveData.value = placeTemporaryLiveData.value?.copy(countryTemp = country)
    }

    fun setRegionTemporary(region: Area?) {
        placeTemporaryLiveData.value = placeTemporaryLiveData.value?.copy(regionTemp = region)
    }

    // Работа с FilterParametersLiveData
    fun setRegion(region: Area?) {
        filterParametersLiveData.value = filterParametersLiveData.value?.copy(region = region)
        placeTemporaryLiveData.value = placeTemporaryLiveData.value?.copy(regionTemp = region)
        saveFiltersToLocalStorage()
    }

    fun setCountry(country: Area?) {
        filterParametersLiveData.value = filterParametersLiveData.value?.copy(country = country)
        placeTemporaryLiveData.value = placeTemporaryLiveData.value?.copy(countryTemp = country)
        saveFiltersToLocalStorage()
    }

    fun setIndustry(industry: Industry?) {
        filterParametersLiveData.value = filterParametersLiveData.value?.copy(industry = industry)
        saveFiltersToLocalStorage()
    }

    fun setExpectedSalary(salary: Int?) {
        filterParametersLiveData.value = filterParametersLiveData.value?.copy(expectedSalary = salary)
        saveFiltersToLocalStorage()
    }

    fun setOnlyWithSalary(onlyWithSalary: Boolean) {
        filterParametersLiveData.value = filterParametersLiveData.value?.copy(onlyWithSalary = onlyWithSalary)
        saveFiltersToLocalStorage()
    }

    fun clearFilters() {
        placeTemporaryLiveData.value = PlaceParameters(null, null)
        filterParametersLiveData.value = emptyFilterParameters
        saveFiltersToLocalStorage()
    }

    fun applyFilters() {
        saveFiltersToLocalStorage()
        filtersAppliedLiveEvent.value = true
        filtersChangedLiveData.value = false
    }

    fun filtersAreEmpty(): Boolean {
        val filters = filterParametersLiveData.value ?: return true
        val areBasicFiltersEmpty = filters.country == null &&
            filters.region == null &&
            filters.industry == null &&
            filters.expectedSalary == null
        return areBasicFiltersEmpty && !filters.onlyWithSalary
    }

}
