package ru.practicum.android.diploma.common.data.mapper

import ru.practicum.android.diploma.common.data.network.dto.AddressDto
import ru.practicum.android.diploma.common.data.network.dto.SalaryDto
import ru.practicum.android.diploma.common.data.network.dto.VacancyDetailsDto
import ru.practicum.android.diploma.common.data.network.dto.VacancyFromListDto
import ru.practicum.android.diploma.common.domain.model.Address
import ru.practicum.android.diploma.common.domain.model.Salary
import ru.practicum.android.diploma.common.domain.model.VacancyDetails
import ru.practicum.android.diploma.common.domain.model.VacancyFromList

class VacancyMapper {

    fun mapVacancyFromList(vacancyFromListDto: VacancyFromListDto): VacancyFromList {
        return with(vacancyFromListDto) {
            VacancyFromList(
                id = id.toLongOrNull() ?: -1L,
                name = name,
                salary = salary?.let { mapSalary(it) },
                areaName = area.name,
                employerName = employer.name,
                employerLogoUrl240 = employer.logoUrls?.logoUrl240
            )
        }
    }

    fun mapVacancyDetails(vacancyDetailsDto: VacancyDetailsDto): VacancyDetails {
        return with(vacancyDetailsDto) {
            VacancyDetails(
                id = id.toLongOrNull() ?: -1L,
                name = name,
                salary = salary?.let { mapSalary(it) },
                areaName = area.name,
                employerName = employer?.name,
                employerLogoUrl240 = employer?.logoUrls?.logoUrl240,
                experience = experience?.name,
                scheduleName = schedule?.name,
                description = description,
                keySkills = keySkills,
                address = address?.let { mapAddress(it) },
                alternateUrl = alternateUrl
            )
        }
    }

    private fun mapSalary(salaryDto: SalaryDto): Salary {
        return with(salaryDto) {
            Salary(
                currency = currency,
                from = from,
                to = to
            )
        }
    }

    private fun mapAddress(addressDto: AddressDto): Address {
        return with(addressDto) {
            Address(
                city = city,
                street = street,
                building = building
            )
        }
    }
}
