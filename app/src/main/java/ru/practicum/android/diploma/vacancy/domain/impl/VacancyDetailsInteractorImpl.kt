package ru.practicum.android.diploma.vacancy.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.common.domain.model.ErrorType
import ru.practicum.android.diploma.common.domain.model.VacancyDetails
import ru.practicum.android.diploma.vacancy.util.Resource
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsRepository

class VacancyDetailsInteractorImpl(
    private val repository: VacancyDetailsRepository
) : VacancyDetailsInteractor {

    override fun getVacancyDetails(vacancyId: Long): Flow<Pair<VacancyDetails?, ErrorType?>> {
        return repository.getVacancyDetails(vacancyId).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> Pair(null, result.errorType)
            }
        }
    }

    override fun shareVacancyUrl(text: String) {
        repository.sendVacancy(text)
    }

    override fun addToFavorites(vacancy: VacancyDetails) {
        repository.addToFavorites(vacancy)
    }

    override fun removeFromFavorites(vacancyId: Long) {
        repository.removeFromFavorites(vacancyId)
    }
}
