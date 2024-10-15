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

    fun showAllRegions(countryId: String?) {
        search(countryId)
    }

    fun showCountries() {
        searchCountry()
    }

    fun searchRequest(searchText: String, countryId: String?) {
        if (searchText.isNotEmpty()) {
            renderState(AreaState.Loading)
            interactor.getRegions()
                .onEach { (regions, errorType) ->
                    when (errorType) {
                        null -> {
                            val filteredRegions = excludeCountries(regions ?: emptyList()).filter {
                                it.name.contains(searchText, ignoreCase = true)
                            }
                            if (countryId != null) filterAreasByParentId(regions ?: emptyList(), countryId)

                            if (filteredRegions.isEmpty()) {
                                renderState(AreaState.NothingFound)
                            } else {
                                renderState(AreaState.Success(filteredRegions))
                            }
                        }

                        ErrorType.CONNECTION_PROBLEM -> renderState(AreaState.InternetError)
                        else -> renderState(AreaState.ServerError)
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun search(countryId: String?) {
        interactor.getRegions()
            .onEach { (searchResult, errorType) ->
                when (errorType) {
                    null -> {
                        if (searchResult.isNullOrEmpty()) {
                            renderState(AreaState.NoList)
                        } else {
                            val regionsOnly = excludeCountries(searchResult)

                            val sortedRegions = sortArea(regionsOnly)
                            if (countryId != null) filterAreasByParentId(searchResult ?: emptyList(), countryId)
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

    private fun searchCountry() {
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
        return area.filter { it.parentId != null && it.parentId != 1001.toString() }
    }

    private fun renderState(state: AreaState) {
        stateLiveData.postValue(state)
    }

    fun filterAreasByParentId(areas: List<Area>, parentId: String?): List<Area> {
        val filteredAreas = areas.filter { it.parentId == parentId }
        val descendantAreas = filteredAreas.flatMap { getDescendants(it, areas) }
        return filteredAreas + descendantAreas
    }

    fun getDescendants(area: Area, areas: List<Area>): List<Area> {
        return areas.filter { it.parentId == area.id }
    }

    companion object {
        const val SEARCH_DELAY = 500L
    }

    private fun sortArea(area: List<Area>): List<Area> {
        val sortedListByName = area.sortedBy { it.name.replace('Ё', 'Е').replace('ё', 'е') }
        val areasWithoutDigits = sortedListByName.filter { !it.name.any { char -> char.isDigit() } }
        val areasWithDigits = sortedListByName.filter { it.name.any { char -> char.isDigit() } }
        return areasWithoutDigits + areasWithDigits // Сначала без цифр, затем с цифрами
    }

    private fun sortCountries(area: List<Area>): List<Area> {
        return area.sortedBy { if (it.name == "Другие регионы") 1 else 0 }
    }

}
