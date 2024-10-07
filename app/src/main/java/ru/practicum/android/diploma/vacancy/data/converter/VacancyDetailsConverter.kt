package ru.practicum.android.diploma.vacancy.data.converter

import ru.practicum.android.diploma.common.data.network.dto.AddressDto
import ru.practicum.android.diploma.common.data.network.dto.SalaryDto
import ru.practicum.android.diploma.common.data.network.dto.VacancyDetailsDto
import ru.practicum.android.diploma.common.domain.model.Address
import ru.practicum.android.diploma.common.domain.model.Salary
import ru.practicum.android.diploma.common.domain.model.VacancyDetails

object VacancyDetailsConverter {

    fun convert(vacancyDetailsDto: VacancyDetailsDto): VacancyDetails {
        return with(vacancyDetailsDto) {
            VacancyDetails(
                id = id.toLongOrNull() ?: -1L,
                name = name,
                salary = salary?.let { convert(it) },
                areaName = area.name,
                employerName = employer?.name,
                employerLogoUrl240 = employer?.logoUrls?.logoUrl240,
                experience = experience?.name,
                scheduleName = schedule?.name,
                description = description,
                keySkills = keySkills,
                address = address?.let { convert(it) },
                alternateUrl = alternateUrl
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

    private fun convert(addressDto: AddressDto): Address {
        return with(addressDto) {
            Address(
                city = city,
                street = street,
                building = building
            )
        }
    }
}
