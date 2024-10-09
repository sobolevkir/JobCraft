package ru.practicum.android.diploma.vacancy.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.domain.model.VacancyDetails
import ru.practicum.android.diploma.vacancy.util.ResourceDetails

interface VacancyDetailsRepository {
    fun shareVacancyUrl(text: String)
    fun addToFavorites(vacancy: VacancyDetails)
    fun removeFromFavorites(vacancyId: Long)
    fun getVacancyDetails(vacancyId: Long): Flow<ResourceDetails<VacancyDetails>>
}
