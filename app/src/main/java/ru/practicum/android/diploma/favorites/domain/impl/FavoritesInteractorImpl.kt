package ru.practicum.android.diploma.favorites.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.domain.model.Vacancy
import ru.practicum.android.diploma.favorites.domain.FavoritesInteractor
import ru.practicum.android.diploma.favorites.domain.FavoritesRepository

class FavoritesInteractorImpl(private val repository: FavoritesRepository) : FavoritesInteractor {
    override suspend fun addVacancyToFavorites(vacancy: Vacancy) {
        repository.addVacancyToFavorites(vacancy)
    }

    override suspend fun updateVacancyInFavorites(vacancy: Vacancy) {
        repository.updateVacancyInFavorites(vacancy)
    }

    override suspend fun removeVacancyFromFavorites(vacancyId: Long) {
        repository.removeVacancyFromFavorites(vacancyId)
    }

    override fun getFavoriteVacancies(): Flow<List<Vacancy>> {
        return repository.getFavoriteVacancies()
    }
}
