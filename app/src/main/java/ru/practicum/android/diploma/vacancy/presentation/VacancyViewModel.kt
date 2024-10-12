package ru.practicum.android.diploma.vacancy.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.practicum.android.diploma.common.domain.model.ErrorType
import ru.practicum.android.diploma.common.domain.model.VacancyDetails
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.vacancy.ui.model.ScreenMode
import ru.practicum.android.diploma.vacancy.ui.model.ScreenState

class VacancyViewModel(
    private val vacancyId: Long,
    private val interactor: VacancyDetailsInteractor,
) : ViewModel() {

    private var isFavorite = false
    private var currentVacancy: VacancyDetails? = null
    private var vacancyLiveData = MutableLiveData(ScreenState(ScreenMode.LOADING, null))
    private var isFavoriteLiveData = MutableLiveData(isFavorite)

    fun getIsFavoriteLiveData(): LiveData<Boolean> = isFavoriteLiveData
    fun getVacancyLiveData(): LiveData<ScreenState> = vacancyLiveData

    fun changeFavorite() {
        currentVacancy?.let { vacancy ->
            isFavorite = !isFavorite
            isFavoriteLiveData.postValue(isFavorite)
            if (isFavorite) {
                addToFavorites(vacancy)
            } else {
                removeFromFavorites(vacancy.id)
            }
        }
    }

    fun setVacancyDetails() {
        interactor.getVacancyDetails(vacancyId)
            .onEach { (data, error) ->
                processingResult(data, error)
            }.launchIn(viewModelScope)
    }

    private fun processingResult(vacancy: VacancyDetails?, errorType: ErrorType?) {
        if (vacancy != null) {
            currentVacancy = vacancy
            isFavorite = currentVacancy!!.isFavorite
            vacancyLiveData.postValue(ScreenState(ScreenMode.RESULTS, vacancy))
            isFavoriteLiveData.postValue(isFavorite)
        } else {
            currentVacancy = null
            when (errorType) {
                ErrorType.NOTHING_FOUND -> {
                    vacancyLiveData.postValue(ScreenState(ScreenMode.NOTHING_FOUND, null))
                }
                ErrorType.CONNECTION_PROBLEM -> {
                    vacancyLiveData.postValue(ScreenState(ScreenMode.CONNECTION_PROBLEM, null))
                }
                else -> {
                    currentVacancy = null
                    vacancyLiveData.postValue(ScreenState(ScreenMode.SERVER_ERROR, null))
                }
            }
        }
    }
    fun shareVacancyUrl() {
        if (currentVacancy != null) {
            interactor.shareVacancyUrl(currentVacancy!!.alternateUrl)
        }
    }

    private fun addToFavorites(vacancy: VacancyDetails) {
        interactor.addToFavorites(vacancy)
    }

    private fun removeFromFavorites(vacancyId: Long) {
        interactor.removeFromFavorites(vacancyId)
    }

}
