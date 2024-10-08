package ru.practicum.android.diploma.common.data.converter

import android.content.Context
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.data.network.dto.AddressDto
import ru.practicum.android.diploma.common.data.network.dto.SalaryDto
import java.text.NumberFormat
import java.util.Locale

class ParametersConverter(private val context: Context) {

    fun convert(salary: SalaryDto): String {
        val numberFormat = NumberFormat.getInstance(Locale("ru", "RU"))

        val currencySymbol = when (salary.currency) {
            "RUR", "RUB" -> context.getString(R.string.currency_rub)
            "BYR" -> context.getString(R.string.currency_byr)
            "USD" -> context.getString(R.string.currency_usd)
            "EUR" -> context.getString(R.string.currency_eur)
            "KZT" -> context.getString(R.string.currency_kzt)
            "UAH" -> context.getString(R.string.currency_uah)
            "AZN" -> context.getString(R.string.currency_azn)
            "UZS" -> context.getString(R.string.currency_uzs)
            "GEL" -> context.getString(R.string.currency_gel)
            "KGT" -> context.getString(R.string.currency_kgt)
            else -> salary.currency ?: context.getString(R.string.currency_default)
        }

        return when {
            salary.from != null && salary.to != null -> context.getString(
                R.string.salary_range,
                numberFormat.format(salary.from),
                numberFormat.format(salary.to),
                currencySymbol
            )

            salary.from != null -> context.getString(
                R.string.salary_from,
                numberFormat.format(salary.from),
                currencySymbol
            )

            salary.to != null -> context.getString(
                R.string.salary_to,
                numberFormat.format(salary.to),
                currencySymbol
            )

            else -> context.getString(R.string.salary_not_specified)
        }
    }

    fun convert(address: AddressDto): String {
        return buildString {
            address.city?.let {
                append(it)
            }
            address.street?.let {
                if (isNotEmpty()) append(context.getString(R.string.address_separator))
                append(it)
            }
            address.building?.let {
                if (isNotEmpty()) append(context.getString(R.string.address_separator))
                append(context.getString(R.string.address_building_prefix, it))
            }

            if (isEmpty()) append(context.getString(R.string.address_not_specified))
        }
    }
}
