package ru.practicum.android.diploma.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.domain.model.ErrorType
import ru.practicum.android.diploma.common.domain.model.VacancyFromList
import ru.practicum.android.diploma.common.presentation.SingleLiveEvent
import ru.practicum.android.diploma.filters.domain.FiltersLocalInteractor
import ru.practicum.android.diploma.search.domain.VacanciesInteractor

class SearchViewModel(
    private val vacanciesInteractor: VacanciesInteractor,
    private val filtersLocalInteractor: FiltersLocalInteractor
) : ViewModel() {

    private var lastRequest: String? = null
    private var searchJob: Job? = null
    private var paddingPage = 0
    private var maxPage = 0
    private var fullList = listOf<VacancyFromList>()
    private var isNextPageLoading = false

    private val stateLiveData = MutableLiveData<SearchState>(SearchState.Default)
    fun getStateLiveData(): LiveData<SearchState> = stateLiveData

    private var showToastEvent = SingleLiveEvent<ErrorType?>()
    fun getToastEvent(): SingleLiveEvent<ErrorType?> = showToastEvent

    fun onQueryChange(queryText: String) {
        searchJob?.cancel()
        if (queryText == lastRequest) {
            return
        }
        lastRequest = queryText
        if (queryText.isEmpty()) {
            viewModelScope.coroutineContext[Job]?.cancelChildren()
            isNextPageLoading = false
            renderState(SearchState.Default)
            return
        }
        searchJob = viewModelScope.launch {
            delay(SEARCH_DELAY_MILLIS)
            newSearch(queryText)
        }
    }

    fun newSearch(queryText: String) {
        searchJob?.cancel()
        if (queryText.isNotEmpty()) {
            paddingPage = 0
            val options = getSearchOptions(queryText, paddingPage)
            renderState(SearchState.Loading)
            vacanciesInteractor.searchVacancies(options).onEach { (searchResult, errorType) ->
                if (errorType == null) {
                    fullList = searchResult?.items ?: emptyList()
                    maxPage = searchResult?.pages ?: 0
                    renderState(SearchState.NewSearchResult(fullList, searchResult?.found ?: 0))
                } else {
                    handleError(errorType)
                }
            }.launchIn(viewModelScope)
        } else {
            renderState(SearchState.Default)
        }
    }

    fun onLastItemReached() {
        searchJob?.cancel()
        if (isNextPageLoading || paddingPage == maxPage - 1 || lastRequest.isNullOrEmpty()) {
            return
        }
        searchJob = viewModelScope.launch {
            delay(NEXT_PAGE_DELAY_MILLIS)
            isNextPageLoading = true
            paddingPage += 1
            renderState(SearchState.NextPageLoading)
            val options = getSearchOptions(lastRequest!!, paddingPage)
            vacanciesInteractor.searchVacancies(options).onEach { (searchResult, errorType) ->
                if (errorType == null) {
                    fullList += searchResult?.items ?: emptyList()
                    maxPage = searchResult?.pages ?: 0
                    renderState(SearchState.NextPageSearchResult(fullList, searchResult?.found ?: 0))
                } else {
                    renderState(SearchState.NextPageError)
                    showToastEvent.postValue(errorType)
                }
                isNextPageLoading = false
            }.launchIn(viewModelScope)
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

    private fun handleError(errorType: ErrorType) {
        when (errorType) {
            ErrorType.CONNECTION_PROBLEM -> renderState(SearchState.InternetError)
            ErrorType.NOTHING_FOUND -> renderState(SearchState.NothingFound)
            else -> renderState(SearchState.ServerError)
        }
    }

    private fun getSearchOptions(
        queryText: String,
        page: Int
    ): MutableMap<String, String> {
        val filters = filtersLocalInteractor.getFilters()
        val areaId = (filters?.region?.id ?: filters?.country?.id).orEmpty()
        val industry = filters?.industry?.id.orEmpty()
        val expectedSalary = filters?.expectedSalary?.toString().orEmpty()
        val onlyWithSalary = if (filters?.onlyWithSalary == true) {
            ONLY_WITH_SALARY_TRUE
        } else {
            ONLY_WITH_SALARY_FALSE
        }
        val options = mutableMapOf(
            OPTION_QUERY_TEXT to queryText,
            OPTION_CURRENT_PAGE to page.toString(),
            OPTION_PER_PAGE to VACANCIES_PER_PAGE_20
        )
        if (areaId.isNotEmpty()) options[OPTION_AREA] = areaId
        if (industry.isNotEmpty()) options[OPTION_INDUSTRY] = industry
        if (expectedSalary.isNotEmpty()) options[OPTION_EXPECTED_SALARY] = expectedSalary
        if (onlyWithSalary == "true") options[OPTION_ONLY_WITH_SALARY] = onlyWithSalary
        return options
    }

    companion object {
        const val SEARCH_DELAY_MILLIS = 1000L
        const val NEXT_PAGE_DELAY_MILLIS = 200L
        const val OPTION_QUERY_TEXT = "text"
        const val OPTION_CURRENT_PAGE = "page"
        const val OPTION_PER_PAGE = "per_page"
        const val OPTION_AREA = "area"
        const val OPTION_INDUSTRY = "industry"
        const val OPTION_EXPECTED_SALARY = "salary"
        const val OPTION_ONLY_WITH_SALARY = "only_with_salary"
        const val ONLY_WITH_SALARY_TRUE = "true"
        const val ONLY_WITH_SALARY_FALSE = "false"
        const val VACANCIES_PER_PAGE_20 = "20"
    }
}
