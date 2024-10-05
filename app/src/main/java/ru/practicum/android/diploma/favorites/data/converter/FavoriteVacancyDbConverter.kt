package ru.practicum.android.diploma.favorites.data.converter

import androidx.room.TypeConverter
import ru.practicum.android.diploma.common.domain.model.Vacancy
import ru.practicum.android.diploma.favorites.data.entity.FavoriteVacancyEntity

class FavoriteVacancyDbConverter {
    @TypeConverter
    fun convert(vacancy: Vacancy): FavoriteVacancyEntity = FavoriteVacancyEntity(
        vacancy.id,
        vacancy.vacancyName,
        vacancy.salary,
        vacancy.region,
        vacancy.employerName,
        vacancy.employerLogoUrl,
        vacancy.experience,
        vacancy.schedule,
        vacancy.description,
        vacancy.keySkills,
    )

    @TypeConverter
    fun convert(favoriteVacancyEntity: FavoriteVacancyEntity) = Vacancy(
        favoriteVacancyEntity.id,
        favoriteVacancyEntity.vacancyName,
        favoriteVacancyEntity.salary,
        favoriteVacancyEntity.region,
        favoriteVacancyEntity.employerName,
        favoriteVacancyEntity.employerLogoUrl,
        favoriteVacancyEntity.experience,
        favoriteVacancyEntity.schedule,
        favoriteVacancyEntity.description,
        favoriteVacancyEntity.keySkills,
        isFavorite = true
    )
}
