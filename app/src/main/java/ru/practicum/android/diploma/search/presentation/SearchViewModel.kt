package ru.practicum.android.diploma.search.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.search.domain.SearchInteractor
import ru.practicum.android.diploma.search.domain.model.VacancyListItem

class SearchViewModel(private val interactor: SearchInteractor) : ViewModel() {

    private val liveDataSearchRes = MutableLiveData<SearchLiveDataObject>()

    private var lastRequest: String? = null
    private var searchJob: Job? = null

    fun getSearchRes(): LiveData<SearchLiveDataObject> = liveDataSearchRes

    fun search(request: String) {
        if (request == lastRequest) {
            return
        }
        lastRequest = request

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DELAY)
            searchRequest(request)
        }
    }

    fun searchTest() {
        viewModelScope.launch {
            val options = mapOf(
                "text" to "android developer",
                "page" to "0",
                "per_page" to "20"
            )
            vacanciesInteractor.searchVacancies(options)
                .collect { (searchResult, errorType) ->
                    if (searchResult != null) {
                        val vacancies = searchResult.items
                        Log.d("vacancies_array", vacancies.toString())
                        Log.d("page_number", searchResult.page.toString())
                        Log.d("vacancies_found_number", searchResult.found.toString())
                    }
                    if (errorType != null) {
                        Log.d("error", errorType.toString())
                    }
                }
        }
    }

    private fun searchRequest(searchText: String) {
        if (searchText.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                interactor.doSearch(searchText).collect{
                    processResult(it)
                }
            }
        }
    }

    private fun processResult(result: List<VacancyListItem>){
        liveDataSearchRes.postValue(SearchLiveDataObject(result))
    }

    companion object{
        const val SEARCH_DELAY = 2000L
    }
}
