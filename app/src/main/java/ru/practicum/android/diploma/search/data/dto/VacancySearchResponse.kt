package ru.practicum.android.diploma.search.data.dto

import com.google.gson.annotations.SerializedName

data class VacancySearchResponse(
    val found: Int,
    val items: List<VacancyFromListDto>,
    val page: Int,
    val pages: Int,
    @SerializedName("per_page") val perPage: Int,
) : Response()
