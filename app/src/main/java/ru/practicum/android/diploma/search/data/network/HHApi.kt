package ru.practicum.android.diploma.search.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import ru.practicum.android.diploma.search.data.dto.Response

interface HHApi {

    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancy(@Path("vacancy_id") id: String): Response
}
