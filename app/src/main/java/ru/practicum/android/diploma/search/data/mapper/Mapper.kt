package ru.practicum.android.diploma.search.data.mapper

import ru.practicum.android.diploma.common.domain.model.Salary
import ru.practicum.android.diploma.common.domain.model.VacancyFromList
import ru.practicum.android.diploma.search.data.dto.SalaryDto
import ru.practicum.android.diploma.search.data.dto.VacancyFromListDto

class Mapper {
    fun VacancyFromListDto.toDomain(): VacancyFromList {
        return VacancyFromList(
            id = this.id,
            name = this.name,
            salary = this.salary?.toDomain(),
            areaName = this.area.name,
            employerName = this.employer.name,
            employerLogoUrl240 = this.employer.logoUrls?.logoUrl
        )
    }

    fun SalaryDto.toDomain(): Salary {
        return Salary(
            currency = this.currency,
            from = this.from,
            to = this.to
        )
    }
}
