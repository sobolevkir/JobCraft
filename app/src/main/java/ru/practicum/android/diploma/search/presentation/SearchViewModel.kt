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

    fun getSearchRes(): LiveData<SearchState> = stateLiveData

    fun setIsSearch(boolean: Boolean) {
        isSearch = boolean
    }

    fun onLastItemReached() {
        if(!isNextPageLoading){
            if (paddingPage != maxPage) {
                isNextPageLoading = true
                paddingPage += 1
                searchRequest(lastRequest!!, paddingPage)
            }
        }
    }

    fun search(request: String) {
        if (request == lastRequest) {
            return
        }
        lastRequest = request
        fullList = listOf()
        paddingPage = 0
        maxPage = 0
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DELAY)
            if (isSearch) {
                isSearch = false
                searchRequest(request, paddingPage)
                bind(SearchState.Loading)
            }
        }
    }

    private fun searchRequest(searchText: String, page: Int) {
        if (searchText.isNotEmpty()) {
            val options = mapOf(
                "text" to searchText,
                "page" to page.toString(),
                "per_page" to "20"
            )
            interactor.searchVacancies(options)
                .onEach { (searchResult, errorType) ->
                    when (errorType) {
                        null -> {
                            fullList += searchResult!!.items
                            bind(SearchState.SearchResult(fullList, searchResult.found))
                            maxPage = searchResult.pages
                        }

                        ErrorType.CONNECTION_PROBLEM -> bind(SearchState.InternetError)

                        ErrorType.NOTHING_FOUND -> bind(SearchState.NothingFound)

                        else -> bind(SearchState.ServerError)
                    }
                    isNextPageLoading = false
                }
                .launchIn(viewModelScope)
        }
    }

    private fun bind(state: SearchState) {
        stateLiveData.postValue(state)
    }

    companion object {
        const val SEARCH_DELAY = 2000L
    }
}
