package ru.practicum.android.diploma.search.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.search.domain.VacanciesInteractor

class SearchViewModel(private val vacanciesInteractor: VacanciesInteractor) : ViewModel() {

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

}
