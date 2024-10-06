package ru.practicum.android.diploma.common.data.mapper

import ru.practicum.android.diploma.common.data.network.dto.SalaryDto
import ru.practicum.android.diploma.common.data.network.dto.VacancyFromListDto
import ru.practicum.android.diploma.common.domain.model.Salary
import ru.practicum.android.diploma.common.domain.model.VacancyFromList

object VacancyMapper {

    fun VacancyFromListDto.toDomain(): VacancyFromList {
        return VacancyFromList(
            id = this.id.toLongOrNull() ?: -1L,
            name = this.name,
            salary = this.salary?.toDomain(),
            areaName = this.area.name,
            employerName = this.employer.name,
            employerLogoUrl240 = this.employer.logoUrls?.logoUrl240
        )
    }

    private fun SalaryDto.toDomain(): Salary {
        return Salary(
            currency = this.currency,
            from = this.from,
            to = this.to
        )
    }

}
