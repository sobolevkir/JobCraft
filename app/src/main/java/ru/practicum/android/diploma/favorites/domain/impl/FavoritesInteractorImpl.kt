package ru.practicum.android.diploma.favorites.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.domain.model.VacancyDetails
import ru.practicum.android.diploma.common.domain.model.VacancyFromList
import ru.practicum.android.diploma.favorites.domain.FavoritesInteractor
import ru.practicum.android.diploma.favorites.domain.FavoritesRepository

class FavoritesInteractorImpl(private val repository: FavoritesRepository) : FavoritesInteractor {
    override suspend fun addVacancyToFavorites(vacancy: VacancyDetails) {
        repository.addVacancyToFavorites(vacancy)
    }

    override suspend fun updateVacancyInFavorites(vacancy: VacancyDetails) {
        repository.updateVacancyInFavorites(vacancy)
    }

    override suspend fun removeVacancyFromFavorites(vacancyId: Long) {
        repository.removeVacancyFromFavorites(vacancyId)
    }

    override fun getFavoriteVacancies(): Flow<List<VacancyFromList>> {
        return repository.getFavoriteVacancies()
    }
}
