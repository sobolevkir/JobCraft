package ru.practicum.android.diploma.filters.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.common.domain.model.ErrorType
import ru.practicum.android.diploma.common.util.Resource
import ru.practicum.android.diploma.filters.domain.AreaInteractor
import ru.practicum.android.diploma.filters.domain.AreaRepository
import ru.practicum.android.diploma.filters.domain.model.Area

class AreaInteractorImpl(private val repository: AreaRepository) : AreaInteractor {
    override fun getAreas(): Flow<Pair<List<Area>?, ErrorType?>> {
        return repository.getAreas().map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> Pair(null, result.errorType)
            }
        }
    }
    override fun getCountries(): Flow<Pair<List<Area>?, ErrorType?>> {
        return repository.getCountries().map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> Pair(null, result.errorType)
            }
        }
    }

}
