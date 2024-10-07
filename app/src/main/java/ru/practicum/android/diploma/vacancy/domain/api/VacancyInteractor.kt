package ru.practicum.android.diploma.vacancy.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.domain.model.VacancyDetails

interface VacancyInteractor {
    fun sendVacancy(text: String)
    fun getVacancyDetails(vacancyId: Long): Flow<Pair<VacancyDetails?, String?>>
    fun addToFavorites (vacancy: VacancyDetails)
    fun removeFromFavorites (vacancyId: Long)
}
