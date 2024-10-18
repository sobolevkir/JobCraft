package ru.practicum.android.diploma.filters.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.practicum.android.diploma.common.domain.model.ErrorType
import ru.practicum.android.diploma.filters.domain.AreaInteractor
import ru.practicum.android.diploma.filters.domain.model.Area
import ru.practicum.android.diploma.filters.presentation.models.AreaState

class RegionViewModel(private val interactor: AreaInteractor) : ViewModel() {
    init {
        getCountries()

    }

    private val stateLiveData = MutableLiveData<AreaState>()
    fun getStateLiveData(): LiveData<AreaState> = stateLiveData

    private var searchedRegions = mutableListOf<Area>()
    private var countries = mutableListOf<Area>()

    fun searchRequest(searchText: String) {
        val filteredRegions = searchedRegions.filter {
            it.name.contains(searchText, ignoreCase = true)
        }
        if (filteredRegions.isEmpty()) {
            renderState(AreaState.NothingFound)
        } else {
            renderState(AreaState.Success(filteredRegions))
        }
    }

    fun getRegions(countryId: String?, name: String?) {
        renderState(AreaState.Loading)
        if (name == OTHER_REGIONS) {
            interactor.getOtherRegions().handleRegionsResponse(countryId)
        } else {
            interactor.getRegions().map { (searchResult, errorType) ->
                if (errorType == null) {
                    searchResult?.let { excludeCountries(it) } to errorType
                } else {
                    searchResult to errorType
                }
            }.handleRegionsResponse(countryId)
        }
    }

    private fun Flow<Pair<List<Area>?, ErrorType?>>.handleRegionsResponse(countryId: String?) {
        this.onEach { (searchResult, errorType) ->
            when (errorType) {
                null -> {
                    if (searchResult.isNullOrEmpty()) {
                        renderState(AreaState.NoList)
                    } else {
                        var sortedRegions = sortRegions(searchResult)
                        if (countryId != null) {
                            sortedRegions = filterAreasByParentId(sortedRegions, countryId)
                        }
                        searchedRegions = sortedRegions.toMutableList()
                        renderState(AreaState.Success(sortedRegions))
                    }
                }

                ErrorType.CONNECTION_PROBLEM -> {
                    renderState(AreaState.InternetError)
                }

                ErrorType.NOTHING_FOUND -> {
                    renderState(AreaState.NothingFound)
                }

                else -> {
                    renderState(AreaState.ServerError)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getCountries() {
        interactor.getCountries().onEach { (searchResult, errorType) ->
            when (errorType) {
                null -> {
                    if (searchResult.isNullOrEmpty()) {
                        renderState(AreaState.NoList)
                    } else {
                        countries = searchResult.toMutableList()
                    }
                }

                ErrorType.CONNECTION_PROBLEM -> {
                    renderState(AreaState.InternetError)
                }

                ErrorType.NOTHING_FOUND -> {
                    renderState(AreaState.NothingFound)
                }

                else -> {
                    renderState(AreaState.ServerError)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getCountryByParentId(parentId: String): Area? {
        return countries.find { it.id == parentId }
    }

    private fun excludeCountries(area: List<Area>): List<Area> {
        return area.filter { it.parentId != OTHER_REGIONS_ID }
    }

    private fun renderState(state: AreaState) {
        stateLiveData.postValue(state)
    }

    private fun filterAreasByParentId(areas: List<Area>, countryId: String?): List<Area> {
        val filteredAreas = areas.filter { it.parentId == countryId }
        return filteredAreas
    }

    private fun sortRegions(area: List<Area>): List<Area> {
        val sortedListByName = area.sortedBy { it.name.replace('Ё', 'Е').replace('ё', 'е') }
        val areasWithoutDigits = sortedListByName.filter { !it.name.any { char -> char.isDigit() } }
        val areasWithDigits = sortedListByName.filter { it.name.any { char -> char.isDigit() } }
        return areasWithoutDigits + areasWithDigits
    }

    companion object {
        private const val OTHER_REGIONS = "Другие регионы"
        private const val OTHER_REGIONS_ID = "1001"
    }
}
