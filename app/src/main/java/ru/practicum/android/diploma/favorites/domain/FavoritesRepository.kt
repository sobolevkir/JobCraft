package ru.practicum.android.diploma.favorites.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.domain.model.Vacancy

interface FavoritesRepository {
    suspend fun addVacancyToFavorites(vacancy: Vacancy)

    suspend fun removeVacancyFromFavorites(vacancyId: Long)

    fun getFavoriteVacancies(): Flow<List<Vacancy>>
}
