package ru.practicum.android.diploma.vacancy.data.impl

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.data.db.AppDatabase
import ru.practicum.android.diploma.common.data.db.dao.VacancyDBConverter
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.domain.model.VacancyDetails
import ru.practicum.android.diploma.common.util.Resource
import ru.practicum.android.diploma.vacancy.domain.ExternalNavigator
import ru.practicum.android.diploma.vacancy.domain.api.VacancyRepository

class VacancyRepositoryImpl (
    private val externalNavigator: ExternalNavigator,
    private val appDatabase: AppDatabase,
    private val dbConvertor: VacancyDBConverter,
    private val networkClient: NetworkClient
) : VacancyRepository {

    override fun sendVacancy(text: String) {
        externalNavigator.shareText(text)
    }

    override fun addToFavorites(vacancy: VacancyDetails) {
        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.favoriteVacancysDao().insertVacancy(dbConvertor.map(vacancy))
        }
    }

    override fun removeFromFavorites(vacancyId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.favoriteVacancysDao().deleteVacancyById(vacancyId)
        }
    }

    override fun updateFavorite(vacancy: VacancyDetails) {
        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.favoriteVacancysDao().updateVacancy(dbConvertor.map(vacancy))
        }
    }

    override fun getVacancyDetails(vacancyId: Long): Flow<Resource<VacancyDetails>> {
        TODO("Not yet implemented")
    }


}
