package ru.practicum.android.diploma.vacancy.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.common.domain.model.ErrorType
import ru.practicum.android.diploma.common.domain.model.VacancyDetails
import ru.practicum.android.diploma.common.util.Resource
import ru.practicum.android.diploma.vacancy.domain.VacancyDetailsInteractor
import ru.practicum.android.diploma.vacancy.domain.VacancyDetailsRepository

class VacancyDetailsInteractorImpl(private val repository: VacancyDetailsRepository) : VacancyDetailsInteractor {

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

}
