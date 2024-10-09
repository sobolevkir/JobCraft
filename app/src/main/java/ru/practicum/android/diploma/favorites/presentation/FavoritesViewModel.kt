package ru.practicum.android.diploma.favorites.presentation

import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.domain.model.VacancyFromList
import ru.practicum.android.diploma.favorites.domain.FavoritesInteractor

class FavoritesViewModel(private val favoritesInteractor: FavoritesInteractor) : ViewModel() {

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
