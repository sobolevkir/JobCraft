package ru.practicum.android.diploma.search.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.search.domain.VacanciesInteractor

class SearchViewModel(private val vacanciesInteractor: VacanciesInteractor) : ViewModel() {

    fun searchTest() {

        Log.d("start!", "START!!!")

        viewModelScope.launch {
            delay(10000)
            vacanciesInteractor.searchVacancies("android developer", 0)
                .collect { (searchResult, errorType) ->
                    if (searchResult != null) {
                        val vacancies = searchResult.items
                        Log.d("vacancies", vacancies.toString())
                    }
                    if (errorType != null) {
                        Log.d("error", errorType.toString())
                    }
                }
        }

    }

}
