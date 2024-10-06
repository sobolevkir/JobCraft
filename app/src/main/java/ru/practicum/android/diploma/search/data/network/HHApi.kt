package ru.practicum.android.diploma.search.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.search.data.dto.Response
import ru.practicum.android.diploma.search.data.dto.VacancySearchResponse

interface HHApi {

    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancy(@Path("vacancy_id") id: String): Response

    @GET("/vacancies")
    suspend fun getVacancies(@QueryMap options: Map<String, String>): VacancySearchResponse // ??
}
