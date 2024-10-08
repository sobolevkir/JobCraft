package ru.practicum.android.diploma.favorites.data.converter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.practicum.android.diploma.common.domain.model.VacancyDetails
import ru.practicum.android.diploma.common.domain.model.VacancyFromList
import ru.practicum.android.diploma.favorites.data.entity.FavoriteVacancyEntity

class FavoriteVacancyDbConverter : KoinComponent {
    private val gson: Gson by inject()

    fun convert(vacancy: VacancyDetails): FavoriteVacancyEntity = FavoriteVacancyEntity(
        vacancy.id,
        vacancy.name,
        vacancy.salary,
        vacancy.areaName,
        vacancy.employerName,
        vacancy.employerLogoUrl240,
        vacancy.experience,
        vacancy.scheduleName,
        vacancy.description,
        gson.toJson(vacancy.keySkills),
        vacancy.address,
        vacancy.alternateUrl
    )

    fun convert(favoriteVacancyEntity: FavoriteVacancyEntity) = VacancyDetails(
        favoriteVacancyEntity.id,
        favoriteVacancyEntity.name,
        favoriteVacancyEntity.salary,
        favoriteVacancyEntity.areaName,
        favoriteVacancyEntity.employerName,
        favoriteVacancyEntity.employerLogoUrl240,
        favoriteVacancyEntity.experience,
        favoriteVacancyEntity.scheduleName,
        favoriteVacancyEntity.description,
        gson.fromJson(favoriteVacancyEntity.keySkills, object : TypeToken<List<String>>() {}.type),
        favoriteVacancyEntity.address,
        favoriteVacancyEntity.alternateUrl,
        isFavorite = true
    )

    fun convertToVacancyFromList(favoriteVacancyEntity: FavoriteVacancyEntity) = VacancyFromList(
        favoriteVacancyEntity.id,
        favoriteVacancyEntity.name,
        favoriteVacancyEntity.salary,
        favoriteVacancyEntity.areaName,
        favoriteVacancyEntity.employerName,
        favoriteVacancyEntity.employerLogoUrl240,
    )
}
