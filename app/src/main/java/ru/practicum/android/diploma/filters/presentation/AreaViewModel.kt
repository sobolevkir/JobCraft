package ru.practicum.android.diploma.filters.presentation

import android.util.Log
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
    // функции в этом классе тестовые //

    fun test() {
        interactor.getRegions()
            .onEach { (data, error) ->
                processingResult(data, error)
            }.launchIn(viewModelScope)

    }

    private fun processingResult(area: List<Area>?, errorType: ErrorType?) {
        if (area != null) {
            area.forEach { Log.d("testList", " ${showCountries(area)} ${showRegions(area)} ") }
        } else {
            when (errorType) {
                ErrorType.NOTHING_FOUND -> {
//                    Log.d("testList", "error")
                }

                else -> {
//                    Log.d("testList", "error")
                }
            }
        }
    }

    private fun showCountries(area: List<Area>): String {
        val stringBuilder = StringBuilder()
        area.forEach {
            stringBuilder.append(it.name).append("\n")

        }
        return stringBuilder.toString()
    }

    private fun showRegions(area: List<Area>): String {
        val stringBuilder = StringBuilder()
        area.forEach {
            if (it.parentId != null) {
                stringBuilder.append(it.name).append("\n")
            }
        }
        return stringBuilder.toString()
    }


    private val stateLiveData = MutableLiveData<AreaState>()
    fun getStateLiveData(): LiveData<AreaState> = stateLiveData

    fun searchOnEditorAction(request: String) {
        //отфильтровать первоначальный список
    }

    fun showAllRegions() {
        search()
    }

    fun searchRequest(searchText: String) {
        if (searchText.isNotEmpty()) {
            renderState(AreaState.Loading)
            search()
        }
    }

    private fun search() {
        interactor.getRegions()
            .onEach { (searchResult, errorType) ->
                when (errorType) {
                    null -> {
                        if (searchResult.isNullOrEmpty()) {
                            Log.d("region", "AreaState.NoList")
                            renderState(AreaState.NoList)  //??
                        } else {
                            Log.d("region", "AreaState.Success $searchResult")
                            renderState(AreaState.Success(searchResult))
                        }
                    }

                    ErrorType.CONNECTION_PROBLEM -> {
                        renderState(AreaState.InternetError)
                        Log.d("region", "AreaState.InternetError")
                    }

                    ErrorType.NOTHING_FOUND -> {
                        Log.d("region", "AreaState.NothingFound")
                        renderState(AreaState.NothingFound)
                    }

                    else -> {
                        Log.d("region", "AreaState.ServerError")
                        renderState(AreaState.ServerError)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun renderState(state: AreaState) {
        stateLiveData.postValue(state)
    }

    companion object {
        const val SEARCH_DELAY = 500L
    }

}
