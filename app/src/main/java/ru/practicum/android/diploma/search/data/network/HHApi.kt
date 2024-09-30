package ru.practicum.android.diploma.search.data.network

import ru.practicum.android.diploma.search.data.dto.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.R

interface HHApi {

    companion object {
        private const val USER_AGENT = "JobCraft (sobolevkir@bk.ru)"
    }

    @Headers("Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: $USER_AGENT")
    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancy(@Path("vacancy_id") id: String): Response
}
