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

    private var isFavorite = false
    private var currentVacancy: VacancyDetails? = null
    private var vacancyLiveData = MutableLiveData(ScreenState(ScreenMode.LOADING, null))
    private var isFavoriteLiveData = MutableLiveData(isFavorite)

    fun getIsFavoriteLiveData(): LiveData<Boolean> = isFavoriteLiveData
    fun getVacancyLiveData(): LiveData<ScreenState> = vacancyLiveData

    fun changeFavorite () {
        if (currentVacancy != null) {
            isFavorite = !isFavorite
            isFavoriteLiveData.postValue(isFavorite)
            if (isFavorite) addToFavorites(currentVacancy!!) else removeFromFavorites(currentVacancy!!.id)
        }

    }

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
            currentVacancy = vacancy
            isFavorite = currentVacancy!!.isFavorite
            vacancyLiveData.postValue(ScreenState(ScreenMode.RESULTS, vacancy))
            isFavoriteLiveData.postValue(isFavorite)
        } else {
            currentVacancy = null
            vacancyLiveData.postValue(ScreenState(ScreenMode.ERROR, null))
        }
    }

    fun shareVacancyUrl() {
        if (currentVacancy != null) {
            interactor.shareVacancyUrl(currentVacancy!!.alternateUrl)
        }
    }

    fun addToFavorites(vacancy: VacancyDetails) {
        interactor.addToFavorites(vacancy)
    }

    fun removeFromFavorites(vacancyId: Long) {
        interactor.removeFromFavorites(vacancyId)
    }

}
