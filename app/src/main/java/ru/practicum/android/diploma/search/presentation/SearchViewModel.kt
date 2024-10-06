package ru.practicum.android.diploma.search.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.search.domain.VacanciesInteractor

class SearchViewModel(private val vacanciesInteractor: VacanciesInteractor) : ViewModel() {

    fun searchTest() {
        val options = mapOf(
            "text" to "android developer",
            "page" to "0",
            "per_page" to "20"
        )
        viewModelScope.launch {
            // Выполняем запрос через интерактор и собираем результат
            vacanciesInteractor.searchVacancies(options)
                .collect { (searchResult, errorType) ->
                    // Результат - это Pair<VacanciesSearchResult?, ErrorType?>
                    if (searchResult != null) {
                        // Успешный результат, можно работать с найденными вакансиями
                        val vacancies = searchResult.items
                        Log.d("vacancies", vacancies.toString())
                    }
                    if (errorType != null) {
                        // Произошла ошибка, можно обработать её
                        Log.d("error", errorType.toString())
                    }
                }
        }

    }

}
