package ru.practicum.android.diploma.search.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.common.domain.model.ErrorType
import ru.practicum.android.diploma.vacancy.util.ResourceDetails
import ru.practicum.android.diploma.search.domain.VacanciesInteractor
import ru.practicum.android.diploma.search.domain.VacanciesRepository
import ru.practicum.android.diploma.search.domain.model.VacanciesSearchResult

class VacanciesInteractorImpl(private val repository: VacanciesRepository) : VacanciesInteractor {

    override fun searchVacancies(options: Map<String, String>): Flow<Pair<VacanciesSearchResult?, ErrorType?>> {
        return repository.searchVacancies(options).map { result ->
            when (result) {
                is ResourceDetails.Success -> {
                    Pair(result.data, null)
                }

                is ResourceDetails.Error -> Pair(null, result.errorType)
            }
        }
    }

}
