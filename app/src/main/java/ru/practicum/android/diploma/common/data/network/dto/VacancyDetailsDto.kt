package ru.practicum.android.diploma.common.data.network.dto

import com.google.gson.annotations.SerializedName

data class VacancyDetailsDto(
    val id: String,
    val name: String,
    val salary: SalaryDto?,
    val area: AreaDto,
    val employer: EmployerDto?,
    val experience: ExperienceDto?,
    val schedule: ScheduleDto?,
    val description: String,
    @SerializedName("key_skills") val keySkills: List<String>,
    val address: AddressDto?,
    @SerializedName("alternate_url") val alternateUrl: String,
)
