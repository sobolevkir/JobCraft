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

class IndustryViewModel(
    private val interactor: IndustryInteractor,
    private val converter: IndustryConverter
) : ViewModel() {

    private val industryState = MutableLiveData<FilterIndustryState>()
    private var selectedId = "-1"
    private var searchText = ""
    private var searchedIndustries = listOf<IndustryForUi>()

    fun getStateLiveData(): LiveData<FilterIndustryState> = industryState

    fun getIndustries() {
        renderState(FilterIndustryState.Loading)
        interactor.getIndustries()
            .onEach { (data, error) ->
                processingResult(data, error)
            }.launchIn(viewModelScope)

    }

    fun getIndustriesWithSelected() {
        if (searchedIndustries.isEmpty()) {
            renderState(FilterIndustryState.InternetError)
        } else {
            var list = mutableListOf<IndustryForUi>()

            searchedIndustries.map {
                list.add(IndustryForUi(it.id, it.name, it.id == selectedId))
            }

            list = list.filter {
                it.name.contains(searchText, ignoreCase = true)
            }.toMutableList()

            if (list.isEmpty()) {
                renderState(FilterIndustryState.NothingFound)
            } else {
                renderState(FilterIndustryState.IndustryFound(list))
            }
        }
    }

    fun saveSearchText(search: String) {
        searchText = search
    }

    private fun processingResult(industry: List<Industry>?, errorType: ErrorType?) {
        when (errorType) {
            null -> {
                if (industry != null) {
                    if (industry.isEmpty()) {
                        renderState(FilterIndustryState.NoList)
                    } else {
                        searchedIndustries = sortIndustries(industry.map { converter.map(it) })
                        getIndustriesWithSelected()
                    }
                }
            }

            ErrorType.NOTHING_FOUND -> {
                renderState(FilterIndustryState.NothingFound)
            }

            ErrorType.CONNECTION_PROBLEM, ErrorType.SERVER_ERROR -> {
                renderState(FilterIndustryState.InternetError)
            }

            else -> {
                renderState(FilterIndustryState.UnknownError)
            }
        }
    }

    fun setSelectedID(id: String) {
        selectedId = id
    }

    private fun renderState(state: FilterIndustryState) {
        industryState.postValue(state)
    }

    private fun sortIndustries(industries: List<IndustryForUi>): List<IndustryForUi> {
        val sortedListByName = industries.sortedBy { it.name.replace('Ё', 'Е').replace('ё', 'е') }
        val areasWithoutDigits = sortedListByName.filter { !it.name.any { char -> char.isDigit() } }
        return areasWithoutDigits
    }

}
