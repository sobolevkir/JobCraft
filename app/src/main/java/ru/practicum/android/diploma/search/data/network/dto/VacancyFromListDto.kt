package ru.practicum.android.diploma.search.data.network.dto

class VacancyFromListDto(
    val id: String,
    val name: String,
    val salary: SalaryDto?,
    val area: AreaDto,
    val employer: EmployerDto
)
