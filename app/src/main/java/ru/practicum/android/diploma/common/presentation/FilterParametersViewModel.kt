package ru.practicum.android.diploma.common.presentation

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

    private var filterParametersLiveData = MutableLiveData<FilterParameters>()
    fun getFilterParametersLiveData(): LiveData<FilterParameters> = filterParametersLiveData

    private var placeTemporaryLiveData = MutableLiveData<PlaceParameters>()
    fun getPlaceTemporaryLiveData(): LiveData<PlaceParameters> = placeTemporaryLiveData

    init {
        getFiltersFromLocalStorage()
    }

    private fun getFiltersFromLocalStorage() {
        filterParametersLiveData.value = filtersLocalInteractor.getFilters() ?: FilterParameters(
            null,
            null,
            null,
            null
        )
        placeTemporaryLiveData.value = PlaceParameters(
            countryTemp = filterParametersLiveData.value?.country,
            regionTemp = filterParametersLiveData.value?.region
        )
    }

    private fun saveFiltersToLocalStorage() {
        filtersLocalInteractor.saveFilters(
            filterParametersLiveData.value ?: FilterParameters(null, null, null, null)
        )
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
        saveFiltersToLocalStorage()
    }

    fun setCountry(country: Area?) {
        filterParametersLiveData.value = filterParametersLiveData.value?.copy(country = country)
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
        filterParametersLiveData.value = FilterParameters(null, null, null, null)
        saveFiltersToLocalStorage()
    }

    fun applyFilters() {
        filtersAppliedLiveEvent.value = true
    }

    fun filtersAreEmpty(): Boolean {
        return filterParametersLiveData.value == null ||
            (filterParametersLiveData.value?.country == null &&
                filterParametersLiveData.value?.region == null &&
                filterParametersLiveData.value?.industry == null &&
                filterParametersLiveData.value?.expectedSalary == null &&
                filterParametersLiveData.value?.onlyWithSalary == false
                ) || (filterParametersLiveData.value?.country == null &&
            filterParametersLiveData.value?.region == null &&
            filterParametersLiveData.value?.industry == null &&
            filterParametersLiveData.value?.expectedSalary == null &&
            filterParametersLiveData.value?.onlyWithSalary == null
            )
    }

}
