package ru.practicum.android.diploma.favorites.presentation

import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.domain.model.VacancyDetails
import ru.practicum.android.diploma.common.domain.model.VacancyFromList
import ru.practicum.android.diploma.favorites.domain.FavoritesInteractor

class FavoritesViewModel(private val favoritesInteractor: FavoritesInteractor) : ViewModel() {
    fun testDb() {
        var startId = 0L
        val vacancyTestList = listOf(
            VacancyDetails(
                startId++,
                "первый",
                null,
                "москва",
                "серг",
                null,
                null,
                "22",
                "ропаа",
                listOf("77", "786"),
                null,
                "",
                false
            ),
            VacancyDetails(
                startId + 2,
                "второй",
                null,
                "питер",
                "леш",
                "https://cdn4.iconfinder.com/data/icons/computer-network-set/512/web_refresh-1024.png",
                null,
                "91",
                "лропалрпа",
                listOf("1876", "2769"),
                null,
                "",
                false
            )
        )
        viewModelScope.launch(Dispatchers.IO) {
            vacancyTestList.forEach { favoritesInteractor.addVacancyToFavorites(it) }
        }
    }

    private val favoriteStateLiveData = MutableLiveData<FavoritesState>()
    fun observeState(): LiveData<FavoritesState> = favoriteStateLiveData

    fun fillData() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                favoritesInteractor
                    .getFavoriteVacancies()
                    .collect { favorites ->
                        processResult(favorites)
                    }
            }
        } catch (ex: SQLiteException) {
            renderState(FavoritesState.Error)
            Log.e("FavoritesViewModel", "Ошибка: ${ex.message}")
        }
    }

    private fun processResult(favorites: List<VacancyFromList>) {
        if (favorites.isEmpty()) {
            renderState(FavoritesState.Empty)
        } else {
            renderState(FavoritesState.Content(favorites))
        }
    }

    private fun renderState(state: FavoritesState) = favoriteStateLiveData.postValue(state)
}
