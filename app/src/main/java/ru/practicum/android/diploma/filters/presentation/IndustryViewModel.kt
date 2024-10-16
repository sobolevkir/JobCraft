package ru.practicum.android.diploma.filters.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.practicum.android.diploma.common.domain.model.ErrorType
import ru.practicum.android.diploma.filters.domain.IndustryInteractor
import ru.practicum.android.diploma.filters.domain.model.Area
import ru.practicum.android.diploma.filters.domain.model.Industry
import ru.practicum.android.diploma.filters.presentation.states.FilterIndustryState

class IndustryViewModel(private val interactor: IndustryInteractor) : ViewModel() {

    private val industryState = MutableLiveData<FilterIndustryState>()

    fun getStateLiveData(): LiveData<FilterIndustryState> = industryState

    fun getIndustries() {
        interactor.getIndustries()
            .onEach { (data, error) ->
                processingResult(data, error)
            }.launchIn(viewModelScope)

    }

    private fun processingResult(industry: List<Industry>?, errorType: ErrorType?) {
        if (industry != null) {
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

    private fun sortAreas(area: List<Industry>): List<Industry> {
        val sortedListByName = area.sortedBy { it.name.replace('Ё', 'Е').replace('ё', 'е') }
        val areasWithoutDigits = sortedListByName.filter { !it.name.any { char -> char.isDigit() } }
        val areasWithDigits = sortedListByName.filter { it.name.any { char -> char.isDigit() } }
        return areasWithoutDigits + areasWithDigits // Сначала без цифр, затем с цифрами
    }

}
