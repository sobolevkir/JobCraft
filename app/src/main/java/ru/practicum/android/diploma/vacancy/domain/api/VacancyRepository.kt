package ru.practicum.android.diploma.vacancy.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.domain.model.VacancyDetails
import ru.practicum.android.diploma.common.util.Resource

interface VacancyRepository {
    fun getVacancyDetails(vacancyId: Long): Flow<Resource<VacancyDetails>>
}
