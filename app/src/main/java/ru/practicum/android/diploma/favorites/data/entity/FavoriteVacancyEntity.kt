package ru.practicum.android.diploma.favorites.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_vacancy_table")
data class FavoriteVacancyEntity(
    @PrimaryKey
    val id: Long,
    val vacancyName: String,
    val salary: String?,
    val region: String,
    val employerName: String,
    val employerLogoUrl: String?,
    val experience: String?,
    val schedule: String?,
    val description: String?,
    val keySkills: String?,
)
