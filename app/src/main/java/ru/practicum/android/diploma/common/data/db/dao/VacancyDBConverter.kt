package ru.practicum.android.diploma.common.data.db.dao

import ru.practicum.android.diploma.common.data.db.entity.FavoriteVacancyEntity
import ru.practicum.android.diploma.common.domain.model.VacancyDetails

class VacancyDBConverter {
    fun map(vacancy: VacancyDetails): FavoriteVacancyEntity {
        return FavoriteVacancyEntity (
            vacancy.id,
            vacancy.name,
            vacancy.salary,
            vacancy.areaName,
            vacancy.employerName,
            vacancy.employerLogoUrl240,
            vacancy.experience,
            vacancy.scheduleName,
            vacancy.description,
            vacancy.keySkills
        )
    }

    fun map(vacancy: FavoriteVacancyEntity): VacancyDetails {
        return VacancyDetails (
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
            "",
            "",
            true
        )
    }
}
