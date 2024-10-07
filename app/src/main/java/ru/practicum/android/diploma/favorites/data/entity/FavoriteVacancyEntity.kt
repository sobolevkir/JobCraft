package ru.practicum.android.diploma.favorites.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_vacancy_table")
data class FavoriteVacancyEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    val salary: String?,
    val areaName: String,
    val employerName: String,
    val employerLogoUrl240: String?,
    val experience: String?,
    val scheduleName: String,
    val description: String,
    val keySkills: String,
    val address: String?,
    val alternateUrl: String
)
