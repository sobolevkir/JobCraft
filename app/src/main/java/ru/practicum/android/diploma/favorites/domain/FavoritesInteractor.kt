package ru.practicum.android.diploma.favorites.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.domain.model.VacancyDetails
import ru.practicum.android.diploma.common.domain.model.VacancyFromList

interface FavoritesInteractor {
    suspend fun addVacancyToFavorites(vacancy: VacancyDetails)
    suspend fun updateVacancyInFavorites(vacancy: VacancyDetails)
    suspend fun removeVacancyFromFavorites(vacancyId: Long)
    fun getFavoriteVacancies(): Flow<List<VacancyFromList>>
}
