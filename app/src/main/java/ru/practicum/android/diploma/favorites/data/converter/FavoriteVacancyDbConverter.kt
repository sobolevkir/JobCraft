package ru.practicum.android.diploma.favorites.data.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.practicum.android.diploma.common.domain.model.Salary
import ru.practicum.android.diploma.common.domain.model.Vacancy
import ru.practicum.android.diploma.favorites.data.entity.FavoriteVacancyEntity

class FavoriteVacancyDbConverter : KoinComponent {
    private val gson: Gson by inject()
    @TypeConverter
    fun convert(vacancy: Vacancy): FavoriteVacancyEntity = FavoriteVacancyEntity(
        vacancy.id,
        vacancy.vacancyName,
        gson.toJson(vacancy.salary),
        vacancy.region,
        vacancy.employerName,
        vacancy.employerLogoUrl,
        vacancy.experience,
        vacancy.schedule,
        vacancy.description,
        gson.toJson(vacancy.keySkills)
    )

    @TypeConverter
    fun convert(favoriteVacancyEntity: FavoriteVacancyEntity) = Vacancy(
        favoriteVacancyEntity.id,
        favoriteVacancyEntity.vacancyName,
        gson.fromJson(favoriteVacancyEntity.salary, object : TypeToken<Salary>() {}.type),
        favoriteVacancyEntity.region,
        favoriteVacancyEntity.employerName,
        favoriteVacancyEntity.employerLogoUrl,
        favoriteVacancyEntity.experience,
        favoriteVacancyEntity.schedule,
        favoriteVacancyEntity.description,
        gson.fromJson(favoriteVacancyEntity.keySkills, object : TypeToken<List<String>>() {}.type),
        isFavorite = true
    )
}
