package ru.practicum.android.diploma.common.data.mapper

import ru.practicum.android.diploma.common.data.network.dto.AddressDto
import ru.practicum.android.diploma.common.data.network.dto.SalaryDto
import ru.practicum.android.diploma.common.data.network.dto.VacancyDetailsResponse
import ru.practicum.android.diploma.common.data.network.dto.VacancyFromListDto
import ru.practicum.android.diploma.common.domain.model.Address
import ru.practicum.android.diploma.common.domain.model.Salary
import ru.practicum.android.diploma.common.domain.model.VacancyDetails
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

    private fun AddressDto.toDomain(): Address {
        return Address(
            city = this.city,
            street = this.street,
            building = this.building
        )
    }

    fun VacancyDetailsResponse.toDomain(): VacancyDetails {
        return VacancyDetails(
            id = this.id.toLongOrNull() ?: -1L,
            name = this.name,
            salary = this.salary?.toDomain(),
            areaName = this.area.name,
            employerName = this.employer?.name,
            employerLogoUrl240 = this.employer?.logoUrls?.logoUrl240,
            experience = this.experience.name,
            scheduleName = this.schedule.name,
            description = this.description,
            keySkills = this.keySkills.map { it.name },
            address = this.address?.toDomain(),
            alternateUrl = this.alternateUrl
        )
    }

}
