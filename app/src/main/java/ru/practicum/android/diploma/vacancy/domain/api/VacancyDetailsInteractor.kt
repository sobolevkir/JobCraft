package ru.practicum.android.diploma.vacancy.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.domain.model.ErrorType
import ru.practicum.android.diploma.common.domain.model.VacancyDetails

interface VacancyDetailsInteractor {
    fun getVacancyDetails(vacancyId: Long): Flow<Pair<VacancyDetails?, ErrorType?>>
    fun sendVacancy(text: String)
    fun addToFavorites (vacancy: VacancyDetails)
    fun removeFromFavorites (vacancyId: Long)
}
