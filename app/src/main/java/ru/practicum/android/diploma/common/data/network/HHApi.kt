package ru.practicum.android.diploma.common.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.common.data.network.dto.AreaFilterDto
import ru.practicum.android.diploma.common.data.network.dto.IndustryDto
import ru.practicum.android.diploma.common.data.network.dto.VacanciesSearchResponse
import ru.practicum.android.diploma.common.data.network.dto.VacancyDetailsResponse

interface HHApi {

    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancyDetails(@Path("vacancy_id") id: String): VacancyDetailsResponse

    @GET("/vacancies")
    suspend fun searchVacancies(@QueryMap options: Map<String, String>): VacanciesSearchResponse

    @GET("/areas")
    suspend fun getAreas(): List<AreaFilterDto>

    @GET("/industries")
    suspend fun getIndustries(): List<IndustryDto>

}
