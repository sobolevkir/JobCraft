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
    private var fullList = listOf<VacancyFromList>()
    private var isSearch = false

    fun getSearchRes(): LiveData<SearchLiveDataObject> = liveDataSearchRes

    fun setIsSearch(boolean: Boolean) {
        isSearch = boolean
        bind(ERROR_500)
    }

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
        isSearch = false
        paddingPage = 1
        maxPage = 0
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DELAY)
            searchRequest(request, paddingPage)
        }
    }

    private fun searchRequest(searchText: String, page: Int) {
        if (searchText.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                interactor.searchVacancies(VacanciesRequestObject(searchText, page))
                    .collect { (searchResult, errorType) ->
                        when (errorType) {
                            null -> {
                                fullList += searchResult!!.items
                                bind(ERROR_200, searchResult.found)
                                maxPage = searchResult.pages
                            }

                            ErrorType.CONNECTION_PROBLEM -> bind(ERROR_401)
                            else -> {
                                fullList = listOf()
                                bind(ERROR_402)
                            }
                        }
                    }
            }
        }
    }

    private fun bind(error: Int, count: Int = 0) {
        liveDataSearchRes.postValue(SearchLiveDataObject(fullList, error, count, isSearch))
    }

    companion object {
        const val SEARCH_DELAY = 2000L
        const val ERROR_200 = 200
        const val ERROR_401 = 401
        const val ERROR_402 = 402
        const val ERROR_500 = 500
    }
}
