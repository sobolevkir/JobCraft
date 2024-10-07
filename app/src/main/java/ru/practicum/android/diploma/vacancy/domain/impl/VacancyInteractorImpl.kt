package ru.practicum.android.diploma.vacancy.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.common.domain.model.VacancyDetails
import ru.practicum.android.diploma.common.util.Resource
import ru.practicum.android.diploma.vacancy.domain.api.VacancyInteractor
import ru.practicum.android.diploma.vacancy.domain.api.VacancyRepository

class VacancyInteractorImpl (
    private val repository: VacancyRepository,
) : VacancyInteractor {

    override fun sendVacancy(text: String) {
        repository.sendVacancy(text)
    }

    override fun addToFavorites(vacancy: VacancyDetails) {
        repository.addToFavorites(vacancy)
    }

    override fun removeFromFavorites(vacancyId: Long) {
        repository.removeFromFavorites(vacancyId)
    }

    override fun getVacancyDetails(vacancyId: Long): Flow<Pair<VacancyDetails?, String?>> {
        return repository.getVacancyDetails (vacancyId).map { result ->
            when(result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }

        }
    }
}
