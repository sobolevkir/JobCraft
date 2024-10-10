package ru.practicum.android.diploma.vacancy.ui.model

import ru.practicum.android.diploma.common.domain.model.VacancyDetails

data class ScreenState(
    val screenMode: ScreenMode,
    val vacancy: VacancyDetails?
)

enum class ScreenMode {
    LOADING,
    RESULTS,
    NOTHING_FOUND,
    SERVER_ERROR
}
