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
import ru.practicum.android.diploma.common.presentation.SingleLiveEvent
import ru.practicum.android.diploma.filters.domain.FiltersLocalInteractor
import ru.practicum.android.diploma.search.domain.VacanciesInteractor
import ru.practicum.android.diploma.search.domain.model.VacanciesSearchResult

class SearchViewModel(
    private val vacanciesInteractor: VacanciesInteractor,
    private val filtersLocalInteractor: FiltersLocalInteractor
) : ViewModel() {

    private var lastRequest: String? = null
    private var searchJob: Job? = null
    private var paddingPage = 0
    private var maxPage = 0
    private var fullList = listOf<VacancyFromList>()
    private var isSearch = false
    private var isNextPageLoading = false
    private var isErrorShown = false

    private val stateLiveData = MutableLiveData<SearchState>()
    fun getStateLiveData(): LiveData<SearchState> = stateLiveData

    private var showToastEvent = SingleLiveEvent<ErrorType>()
    fun getToastEvent(): SingleLiveEvent<ErrorType> = showToastEvent

    init {
        stateLiveData.postValue(SearchState.Default)
    }

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
        if (request.isEmpty()) {
            renderState(SearchState.Default)
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

    fun newSearch(request: String) {
        paddingPage = 0
        searchJob?.cancel()
        searchRequest(request, paddingPage, isNew = true)
    }

    private fun searchRequest(searchText: String, page: Int, isNew: Boolean) {
        if (searchText.isNotEmpty()) {
            val filters = filtersLocalInteractor.getFilters()
            val areaId = (filters?.region?.id ?: filters?.country?.id).orEmpty()
            val industry = filters?.industry?.id.orEmpty()
            val salary = filters?.expectedSalary?.toString().orEmpty()
            val onlyWithSalary = if (filters?.onlyWithSalary == true) "true" else "false"
            val options = mutableMapOf(
                "text" to searchText,
                "page" to page.toString(),
                "per_page" to "20"
            )
            if (areaId.isNotEmpty()) options["area"] = areaId
            if (industry.isNotEmpty()) options["industry"] = industry
            if (salary.isNotEmpty()) options["salary"] = salary
            if (onlyWithSalary == "true") options["only_with_salary"] = onlyWithSalary
            if (isNew) {
                renderState(SearchState.Loading)
            } else {
                renderState(SearchState.Updating)
            } /*Log.d("SEARCH!!!", "-> area - ${options["area"].toString()}")
            Log.d("SEARCH!!!", "-> salary - ${options["salary"].toString()}")
            Log.d("SEARCH!!!", "-> onlyWithSalary - ${options["only_with_salary"].toString()}")*/
            vacanciesInteractor.searchVacancies(options).onEach { (searchResult, errorType) ->
                searchVacancies(searchResult, errorType, isNew)
            }.launchIn(viewModelScope)
        } else {
            renderState(SearchState.Default)
        }
    }

    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }

    fun applyFilters() {
        val request = lastRequest
        if (!request.isNullOrEmpty()) {
            newSearch(request)
        }
    }

    private fun searchVacancies(searchResult: VacanciesSearchResult?, errorType: ErrorType?, isNew: Boolean) {
        if (errorType == null) {
            isErrorShown = false
            if (isNew) {
                fullList = searchResult!!.items
            } else {
                fullList += searchResult!!.items
            }
            renderState(SearchState.SearchResult(fullList, searchResult.found))
            maxPage = searchResult.pages
        } else {
            handleError(errorType, isNew)
        }
        isNextPageLoading = false
    }

    private fun handleError(errorType: ErrorType, isNew: Boolean) {
        if (isNew) {
            when (errorType) {
                ErrorType.CONNECTION_PROBLEM -> renderState(SearchState.InternetError)

                ErrorType.NOTHING_FOUND -> renderState(SearchState.NothingFound)

                else -> renderState(SearchState.ServerError)
            }
        } else {
            if (!isErrorShown) {
                showToastEvent.value = errorType
                isErrorShown = true
            }
        }
    }

    companion object {
        const val SEARCH_DELAY = 2000L
    }
}
