package ru.practicum.android.diploma.filters.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.practicum.android.diploma.common.domain.model.ErrorType
import ru.practicum.android.diploma.filters.domain.AreaInteractor
import ru.practicum.android.diploma.filters.domain.model.Area

class AreaViewModel(private val interactor: AreaInteractor) : ViewModel() {

    private val stateLiveData = MutableLiveData<AreaState>()
    fun getStateLiveData(): LiveData<AreaState> = stateLiveData

    private var searchedRegions = mutableListOf<Area>()

    fun showCountries() {
        getCountries()
    }

    fun searchRequest(searchText: String) {
        if (searchText.isNotEmpty()) {
            val filteredRegions = excludeCountries(searchedRegions).filter {
                it.name.contains(searchText, ignoreCase = true)
            }
            if (filteredRegions.isEmpty()) {
                renderState(AreaState.NothingFound)
            } else {
                renderState(AreaState.Success(filteredRegions))
            }
        }
    }

    fun getRegions(countryId: String?) {
        interactor.getRegions()
            .onEach { (searchResult, errorType) ->
                when (errorType) {
                    null -> {
                        if (searchResult.isNullOrEmpty()) {
                            renderState(AreaState.NoList)
                        } else {
                            val regionsOnly = excludeCountries(searchResult)
                            var sortedRegions = sortAreas(regionsOnly)
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
            }
            .launchIn(viewModelScope)
    }

    private fun getCountries() {
        interactor.getCountries()
            .onEach { (searchResult, errorType) ->
                when (errorType) {
                    null -> {
                        if (searchResult.isNullOrEmpty()) {
                            renderState(AreaState.NoList) // ??
                        } else {
                            val sortedCountries = sortCountries(searchResult)
                            renderState(AreaState.Success(sortedCountries))
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
            }
            .launchIn(viewModelScope)
    }

    private fun excludeCountries(area: List<Area>): List<Area> {
        return area.filter { it.parentId != null && it.parentId != "1001" }
    }

    private fun renderState(state: AreaState) {
        stateLiveData.postValue(state)
    }

    private fun filterAreasByParentId(areas: List<Area>, countryId: String?): List<Area> {
        val filteredAreas = areas.filter { it.parentId == countryId }
        return filteredAreas
    }

    private fun sortAreas(area: List<Area>): List<Area> {
        val sortedListByName = area.sortedBy { it.name.replace('Ё', 'Е').replace('ё', 'е') }
        val areasWithoutDigits = sortedListByName.filter { !it.name.any { char -> char.isDigit() } }
        val areasWithDigits = sortedListByName.filter { it.name.any { char -> char.isDigit() } }
        return areasWithoutDigits + areasWithDigits // Сначала без цифр, затем с цифрами
    }

    private fun sortCountries(area: List<Area>): List<Area> {
        return area.sortedBy { if (it.name == "Другие регионы") 1 else 0 }
    }

}
