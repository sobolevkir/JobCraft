package ru.practicum.android.diploma.vacancy.data.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.data.converter.ParametersConverter
import ru.practicum.android.diploma.common.data.db.AppDatabase
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.data.network.dto.ResultCode
import ru.practicum.android.diploma.common.data.network.dto.VacancyDetailsRequest
import ru.practicum.android.diploma.common.data.network.dto.VacancyDetailsResponse
import ru.practicum.android.diploma.common.domain.model.ErrorType
import ru.practicum.android.diploma.common.domain.model.VacancyDetails
import ru.practicum.android.diploma.vacancy.domain.ExternalNavigator
import ru.practicum.android.diploma.vacancy.util.Resource
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsRepository

class VacancyDetailsRepositoryImpl(
    private val externalNavigator: ExternalNavigator,
    private val appDatabase: AppDatabase,
    private val dbConvertor: FavoriteVacancyDbConverter,
    private val networkClient: NetworkClient,
    private val ioDispatcher: CoroutineDispatcher,
    private val parametersConverter: ParametersConverter
) : VacancyDetailsRepository {

    override fun sendVacancy(text: String) {
        externalNavigator.shareText(text)
    }

    override fun addToFavorites(vacancy: VacancyDetails) {
        CoroutineScope(ioDispatcher).launch {
            appDatabase.favoriteVacancysDao().insertVacancy(dbConvertor.map(vacancy))
        }
    }

    override fun removeFromFavorites(vacancyId: Long) {
        CoroutineScope(ioDispatcher).launch {
            appDatabase.favoriteVacancysDao().deleteVacancyById(vacancyId)
        }
    }

    private fun updateFavorite(vacancy: VacancyDetails) {
        CoroutineScope(ioDispatcher).launch {
            appDatabase.favoriteVacancysDao().updateVacancy(dbConverter.convert(vacancy))
        }
    }

    override fun getVacancyDetails(vacancyId: Long): Flow<Resource<VacancyDetails>> = flow<Resource<VacancyDetails>> {
        val favoritesIdList = appDatabase.favoriteVacancysDao().getFavoritesIdsList()
        val isFavorite = favoritesIdList.any { it == vacancyId }

        val response = networkClient.doRequest(VacancyDetailsRequest(vacancyId))
        when (response.resultCode) {
            ResultCode.SUCCESS -> {
                val vacancyDetailsResponse = response as VacancyDetailsResponse
                val resultData = vacancyDetailsResponse.convertToVacancyDetails()
                emit(Resource.Success(resultData))
                if (isFavorite) { updateFavorite(resultData) }
            }

            ResultCode.CONNECTION_PROBLEM -> {
                if (isFavorite) {
                    val resultData = dbConverter.convert(appDatabase.favoriteVacancysDao().getVacancy(vacancyId))
                    emit(Resource.Success(resultData))
                } else {
                    emit(Resource.Error(ErrorType.CONNECTION_PROBLEM))
                }
            }
            ResultCode.NOTHING_FOUND -> {
                emit(Resource.Error(ErrorType.NOTHING_FOUND))
                if (isFavorite) appDatabase.favoriteVacancysDao().deleteVacancyById(vacancyId)
            }
            ResultCode.BAD_REQUEST -> emit(Resource.Error(ErrorType.BAD_REQUEST))
            ResultCode.SERVER_ERROR -> emit(Resource.Error(ErrorType.SERVER_ERROR))
            ResultCode.FORBIDDEN_ERROR -> emit(Resource.Error(ErrorType.FORBIDDEN_ERROR))
            else -> emit(Resource.Error(ErrorType.UNKNOWN_ERROR))
        }

    }.flowOn(ioDispatcher)

    private fun VacancyDetailsResponse.convertToVacancyDetails(): VacancyDetails {
        return with(this) {
            VacancyDetails(
                id = id.toLongOrNull() ?: -1L,
                name = name,
                salary = salary?.let { salaryDto -> parametersConverter.convert(salaryDto) },
                areaName = area.name,
                employerName = employer?.name,
                employerLogoUrl240 = employer?.logoUrls?.logoUrl240,
                experience = experience?.name,
                scheduleName = schedule?.name,
                description = description,
                keySkills = keySkills.map { it.name },
                address = address?.let { addressDto -> parametersConverter.convert(addressDto) },
                alternateUrl = alternateUrl
            )
        }
    }


}
