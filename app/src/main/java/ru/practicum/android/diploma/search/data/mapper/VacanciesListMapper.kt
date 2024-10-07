package ru.practicum.android.diploma.search.data.mapper

import ru.practicum.android.diploma.common.data.network.dto.SalaryDto
import ru.practicum.android.diploma.common.data.network.dto.VacancyFromListDto
import ru.practicum.android.diploma.common.domain.model.Salary
import ru.practicum.android.diploma.common.domain.model.VacancyFromList

object VacanciesListMapper {

    fun map(vacanciesFromListDto: List<VacancyFromListDto>): List<VacancyFromList> {
        return vacanciesFromListDto.map {
            VacancyFromList(
                id = it.id.toLongOrNull() ?: -1L,
                name = it.name,
                salary = it.salary?.let { convert(it) },
                areaName = it.area.name,
                employerName = it.employer.name,
                employerLogoUrl240 = it.employer.logoUrls?.logoUrl240
            )
        }
    }

    private fun convert(salaryDto: SalaryDto): Salary {
        return with(salaryDto) {
            Salary(
                currency = currency,
                from = from,
                to = to
            )
        }
    }
}
