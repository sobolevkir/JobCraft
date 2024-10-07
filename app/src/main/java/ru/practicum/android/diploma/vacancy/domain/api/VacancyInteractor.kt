package ru.practicum.android.diploma.vacancy.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.domain.model.VacancyDetails

interface VacancyInteractor {
    fun getVacancyDetails(vacancyId: Long): Flow<Pair<VacancyDetails?, String?>>
}
