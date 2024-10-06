package ru.practicum.android.diploma.search.data.dto

class VacancyFromListDto(
    val id: Long,
    val name: String,
    val salary: SalaryDto?,
    val area: AreaDto,
    val employer: EmployerDto
)
