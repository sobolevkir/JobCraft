package ru.practicum.android.diploma.common.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_vacancy_table")
data class FavoriteVacancyEntity(
    @PrimaryKey
    val id: String,
    val vacancyName: String,
    val salary: String?,
    val area: String,
    val employerName: String,
    val employerLogoUrl: String?,
    val experience: String?,
    val schedule: String?,
    val description: String?,
    val keySkills: String?
)
