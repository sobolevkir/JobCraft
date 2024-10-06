package ru.practicum.android.diploma.favorites.domain

import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    suspend fun addVacancyToFavorites(vacancy: Vacancy)
    suspend fun updateVacancyInFavorites(vacancy: Vacancy)
    suspend fun removeVacancyFromFavorites(vacancyId: Long)
    fun getFavoriteVacancies(): Flow<List<Vacancy>>
}
