package ru.practicum.android.diploma.vacancy.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.vacancy.domain.VacancyDetailsInteractor

class VacancyViewModel(private val vacancyDetailsInteractor: VacancyDetailsInteractor) : ViewModel() {

    fun getVacancyDetailsTest() {
        viewModelScope.launch {
            val vacancyId = 108187747L
            vacancyDetailsInteractor.getVacancyDetails(vacancyId)
                .collect { (vacancyDetails, errorType) ->
                    if (vacancyDetails != null) {
                        Log.d("vacancy_details", vacancyDetails.toString())
                    }
                    if (errorType != null) {
                        Log.d("error", errorType.toString())
                    }
                }
        }

    }

}
