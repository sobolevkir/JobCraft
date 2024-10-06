package ru.practicum.android.diploma.search.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.search.data.network.dto.Response
import ru.practicum.android.diploma.search.data.network.dto.VacanciesSearchResponse

interface HHApi {

    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancy(@Path("vacancy_id") id: String): Response

    @GET("/vacancies")
    suspend fun searchVacancies(
        @Query("text") queryText: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): VacanciesSearchResponse
}
