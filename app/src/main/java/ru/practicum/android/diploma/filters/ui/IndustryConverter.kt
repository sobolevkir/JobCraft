package ru.practicum.android.diploma.filters.ui

import ru.practicum.android.diploma.filters.domain.model.Industry

class IndustryConverter {
    fun map(industry: Industry): IndustryForUi{
        return IndustryForUi(industry.id,
            industry.name)
    }

    fun map(industry: IndustryForUi): Industry{
        return Industry(industry.id,
            industry.name)
    }
}
