package ru.practicum.android.diploma.vacancy.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.domain.model.ErrorType
import ru.practicum.android.diploma.common.domain.model.VacancyDetails
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.vacancy.ui.model.ScreenMode
import ru.practicum.android.diploma.vacancy.ui.model.ScreenState

class VacancyViewModel(
    private val interactor: VacancyDetailsInteractor,
) : ViewModel() {

    private var vacancyLiveData = MutableLiveData(ScreenState(ScreenMode.LOADING, null))

    fun getVacancyLiveData(): LiveData<ScreenState> = vacancyLiveData

    fun getVacancyDetails(vacancyId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor
                .getVacancyDetails(vacancyId)
                .collect { pair ->
                    processingResult(pair.first, pair.second)
                }
        }
    }

    private fun processingResult(vacancy: VacancyDetails?, errorType: ErrorType?) {
        if (vacancy != null) {
            vacancyLiveData.postValue(ScreenState(ScreenMode.RESULTS, vacancy))
        } else {
            vacancyLiveData.postValue(ScreenState(ScreenMode.ERROR, null))
        }
    }

    fun shareVacancyUrl(text: String) {
        interactor.shareVacancyUrl(text)
    }

    fun addToFavorites(vacancy: VacancyDetails) {
        interactor.addToFavorites(vacancy)
    }

    fun removeFromFavorites(vacancyId: Long) {
        interactor.removeFromFavorites(vacancyId)
    }

}
