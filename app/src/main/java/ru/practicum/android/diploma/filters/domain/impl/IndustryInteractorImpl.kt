package ru.practicum.android.diploma.filters.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.common.domain.model.ErrorType
import ru.practicum.android.diploma.common.util.Resource
import ru.practicum.android.diploma.filters.domain.IndustryInteractor
import ru.practicum.android.diploma.filters.domain.IndustryRepository
import ru.practicum.android.diploma.filters.domain.model.Industry

class IndustryInteractorImpl(private val repository: IndustryRepository) : IndustryInteractor {
    override fun getIndustries(): Flow<Pair<List<Industry>?, ErrorType?>> {
        return repository.getIndustries().map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> Pair(null, result.errorType)
            }
        }
    }

}
