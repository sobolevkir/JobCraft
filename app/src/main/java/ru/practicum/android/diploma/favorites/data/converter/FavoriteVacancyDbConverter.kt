package ru.practicum.android.diploma.favorites.data.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.practicum.android.diploma.common.domain.model.Address
import ru.practicum.android.diploma.common.domain.model.Salary
import ru.practicum.android.diploma.common.domain.model.VacancyDetails
import ru.practicum.android.diploma.common.domain.model.VacancyFromList
import ru.practicum.android.diploma.favorites.data.entity.FavoriteVacancyEntity

class FavoriteVacancyDbConverter : KoinComponent {
    private val gson: Gson by inject()

    @TypeConverter
    fun convert(vacancy: VacancyDetails): FavoriteVacancyEntity = FavoriteVacancyEntity(
        vacancy.id,
        vacancy.name,
        gson.toJson(vacancy.salary),
        vacancy.areaName,
        vacancy.employerName,
        vacancy.employerLogoUrl240,
        vacancy.experience,
        vacancy.scheduleName,
        vacancy.description,
        gson.toJson(vacancy.keySkills),
        gson.toJson(vacancy.address),
        vacancy.alternateUrl
    )

    @TypeConverter
    fun convert(favoriteVacancyEntity: FavoriteVacancyEntity) = VacancyDetails(
        favoriteVacancyEntity.id,
        favoriteVacancyEntity.name,
        gson.fromJson(favoriteVacancyEntity.salary, object : TypeToken<Salary>() {}.type),
        favoriteVacancyEntity.areaName,
        favoriteVacancyEntity.employerName,
        favoriteVacancyEntity.employerLogoUrl240,
        favoriteVacancyEntity.experience,
        favoriteVacancyEntity.scheduleName,
        favoriteVacancyEntity.description,
        gson.fromJson(favoriteVacancyEntity.keySkills, object : TypeToken<List<String>>() {}.type),
        gson.fromJson(favoriteVacancyEntity.address, object : TypeToken<Address>() {}.type),
        favoriteVacancyEntity.alternateUrl,
        isFavorite = true
    )

    fun convertToVacancyFromList(favoriteVacancyEntity: FavoriteVacancyEntity) = VacancyFromList(
        favoriteVacancyEntity.id,
        favoriteVacancyEntity.name,
        gson.fromJson(favoriteVacancyEntity.salary, object : TypeToken<Salary>() {}.type),
        favoriteVacancyEntity.areaName,
        favoriteVacancyEntity.employerName,
        favoriteVacancyEntity.employerLogoUrl240,
    )
}
