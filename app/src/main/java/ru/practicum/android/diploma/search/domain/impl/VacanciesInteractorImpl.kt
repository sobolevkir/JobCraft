package ru.practicum.android.diploma.search.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.common.domain.model.ErrorType
import ru.practicum.android.diploma.common.util.Resource
import ru.practicum.android.diploma.search.domain.VacanciesInteractor
import ru.practicum.android.diploma.search.domain.VacanciesRepository
import ru.practicum.android.diploma.search.domain.model.VacanciesSearchResult

class VacanciesInteractorImpl(private val repository: VacanciesRepository) : VacanciesInteractor {

    override fun searchVacancies(options: Map<String, String>): Flow<Pair<VacanciesSearchResult?, ErrorType?>> {
        return repository.searchVacancies(options).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> Pair(null, result.errorType)
            }
        }
    }

}
