package ru.practicum.android.diploma.filters.presentation

import ru.practicum.android.diploma.filters.domain.model.Industry
import ru.practicum.android.diploma.filters.ui.IndustryForUi

class IndustryConverter {
    fun map(industry: Industry): IndustryForUi {
        return IndustryForUi(
            industry.id,
            industry.name
        )
    }

    fun map(industry: IndustryForUi): Industry {
        return Industry(
            industry.id,
            industry.name
        )
    }
}
