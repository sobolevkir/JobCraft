package ru.practicum.android.diploma.filters.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.practicum.android.diploma.common.domain.model.ErrorType
import ru.practicum.android.diploma.filters.domain.IndustryInteractor
import ru.practicum.android.diploma.filters.domain.model.Industry
import ru.practicum.android.diploma.filters.presentation.models.FilterIndustryState
import ru.practicum.android.diploma.filters.ui.IndustryForUi

class IndustryViewModel(private val interactor: IndustryInteractor) : ViewModel() {

    private val industryState = MutableLiveData<FilterIndustryState>()

    private var searchedIndustries = listOf<Industry>()

    fun getStateLiveData(): LiveData<FilterIndustryState> = industryState

    fun getIndustries() {
        renderState(FilterIndustryState.Loading)
        interactor.getIndustries()
            .onEach { (data, error) ->
                processingResult(data, error)
            }.launchIn(viewModelScope)

    }

    fun searchRequest(search: String) {
        if (search.isNotEmpty()) {
            val filteredRegions = searchedIndustries.filter {
                it.name.contains(search, ignoreCase = true)
            }
            if (filteredRegions.isEmpty()) {
                renderState(FilterIndustryState.NothingFound)
            } else {
                renderState(FilterIndustryState.IndustryFound(filteredRegions))
            }
        }
    }

    private fun processingResult(industry: List<Industry>?, errorType: ErrorType?) {
        if (industry != null) {
            searchedIndustries = sortIndustries(industry)
            renderState(FilterIndustryState.IndustryFound(industry))
        } else {
            when (errorType) {
                ErrorType.NOTHING_FOUND -> {
                    renderState(FilterIndustryState.NothingFound)
                }

                ErrorType.CONNECTION_PROBLEM -> {
                    renderState(FilterIndustryState.InternetError)
                }

                else -> {
                    renderState(FilterIndustryState.UnknownError)
                }
            }
        }
    }

    private fun renderState(state: FilterIndustryState) {
        industryState.postValue(state)
    }

    private fun sortIndustries(industries: List<Industry>): List<Industry> {
        val sortedListByName = industries.sortedBy { it.name.replace('Ё', 'Е').replace('ё', 'е') }
        val areasWithoutDigits = sortedListByName.filter { !it.name.any { char -> char.isDigit() } }
        return areasWithoutDigits
    }

}
