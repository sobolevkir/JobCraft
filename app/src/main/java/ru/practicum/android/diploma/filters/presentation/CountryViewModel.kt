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
import ru.practicum.android.diploma.filters.presentation.models.AreaState

class CountryViewModel(private val interactor: AreaInteractor) : ViewModel() {

    private val stateLiveData = MutableLiveData<AreaState>()
    fun getStateLiveData(): LiveData<AreaState> = stateLiveData

    fun getCountries() {
        renderState(AreaState.Loading)
        interactor.getCountries()
            .onEach { (searchResult, errorType) ->
                when (errorType) {
                    null -> {
                        if (searchResult.isNullOrEmpty()) {
                            renderState(AreaState.NoList)
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

                    ErrorType.SERVER_ERROR -> {
                        renderState(AreaState.ServerError)
                    }

                    else -> {
                        renderState(AreaState.NoList)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun renderState(state: AreaState) {
        stateLiveData.postValue(state)
    }

    private fun sortCountries(area: List<Area>): List<Area> {
        return area.sortedBy { if (it.name == OTHER_REGIONS) 1 else 0 }
    }

    companion object {
        private const val OTHER_REGIONS = "Другие регионы"
    }

}
