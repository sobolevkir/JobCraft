package ru.practicum.android.diploma.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.domain.model.ErrorType
import ru.practicum.android.diploma.common.domain.model.VacancyFromList
import ru.practicum.android.diploma.search.domain.VacanciesInteractor

class SearchViewModel(private val interactor: VacanciesInteractor) : ViewModel() {

    private val stateLiveData = MutableLiveData<SearchState>()

    private var lastRequest: String? = null
    private var searchJob: Job? = null
    private var paddingPage = 0
    private var maxPage = 0
    private var fullList = listOf<VacancyFromList>()
    private var isSearch = false
    private var isNextPageLoading = false

    fun getStateLiveData(): LiveData<SearchState> = stateLiveData

    fun onLastItemReached() {
        if (!isNextPageLoading && paddingPage != maxPage - 1) {
            isNextPageLoading = true
            paddingPage += 1
            searchRequest(lastRequest!!, paddingPage, isNew = false)
        }
    }

    fun search(request: String) {
        if (request == lastRequest) {
            return
        }
        isSearch = request.isNotEmpty()
        lastRequest = request
        fullList = listOf()
        paddingPage = 0
        maxPage = 0
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DELAY)
            if (isSearch) {
                searchRequest(request, paddingPage, isNew = true)
                isSearch = false
            }
        }
    }

    fun searchOnEditorAction(request: String) {
        paddingPage = 0
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DELAY)
            searchRequest(request, paddingPage, isNew = true)
        }
    }

    private fun searchRequest(searchText: String, page: Int, isNew: Boolean) {
        if (searchText.isNotEmpty()) {
            val options = mapOf(
                "text" to searchText,
                "page" to page.toString(),
                "per_page" to "20"
            )

            if (isNew) {
                renderState(SearchState.Loading)
            } else {
                renderState(SearchState.Updating)
            }

            interactor.searchVacancies(options)
                .onEach { (searchResult, errorType) ->
                    when (errorType) {
                        null -> {
                            fullList += searchResult!!.items
                            renderState(SearchState.SearchResult(fullList, searchResult.found))
                            maxPage = searchResult.pages
                        }

                        ErrorType.CONNECTION_PROBLEM -> renderState(SearchState.InternetError)

                        ErrorType.NOTHING_FOUND -> renderState(SearchState.NothingFound)

                        else -> renderState(SearchState.ServerError)
                    }
                    isNextPageLoading = false
                }
                .launchIn(viewModelScope)
        }
    }

    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }

    companion object {
        const val SEARCH_DELAY = 500L
    }
}
