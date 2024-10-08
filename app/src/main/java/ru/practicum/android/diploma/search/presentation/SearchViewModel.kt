package ru.practicum.android.diploma.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.domain.model.ErrorType
import ru.practicum.android.diploma.common.domain.model.VacancyFromList
import ru.practicum.android.diploma.search.domain.VacanciesInteractor
import ru.practicum.android.diploma.search.domain.model.VacanciesRequestObject

class SearchViewModel(private val interactor: VacanciesInteractor) : ViewModel() {

    private val liveDataSearchRes = MutableLiveData<SearchLiveDataObject>()

    private var lastRequest: String? = null
    private var searchJob: Job? = null
    private var paddingPage = 1
    private var maxPage = 0

    fun getSearchRes(): LiveData<SearchLiveDataObject> = liveDataSearchRes

    fun onLastItemReached() {
        if (paddingPage != maxPage) {
            paddingPage += 1
            searchRequest(lastRequest!!, paddingPage)
        }
    }

    fun search(request: String) {
        if (request == lastRequest) {
            return
        }
        lastRequest = request

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DELAY)
            searchRequest(request, paddingPage)
        }
    }

    fun searchTest() {
        viewModelScope.launch {

        }
    }

    private fun searchRequest(searchText: String, page: Int) {
        if (searchText.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                interactor.searchVacancies(VacanciesRequestObject(searchText, page))
                    .collect { (searchResult, errorType) ->
                        when (errorType) {
                            null -> {
                                bind(200, searchResult!!.items)
                                maxPage = searchResult.pages
                            }

                            ErrorType.CONNECTION_PROBLEM -> bind(401)
                            else -> bind(400)
                        }
                    }
            }
        }
    }

    private fun bind(error: Int, list: List<VacancyFromList> = listOf()) {
        liveDataSearchRes.postValue(SearchLiveDataObject(list, error))
    }

    companion object {
        const val SEARCH_DELAY = 2000L
    }
}
