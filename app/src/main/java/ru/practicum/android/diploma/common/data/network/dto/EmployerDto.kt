package ru.practicum.android.diploma.common.data.network.dto

import com.google.gson.annotations.SerializedName

data class EmployerDto(
    val name: String,
    @SerializedName("logo_urls") val logoUrls: LogoUrlsDto?
)
