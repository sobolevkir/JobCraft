package ru.practicum.android.diploma.common.domain.model

data class Salary(
    val currency: String,
    val from: Double,
    val gross: Boolean,
    val to: Double?
)
