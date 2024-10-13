package ru.practicum.android.diploma.filters.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.practicum.android.diploma.common.domain.model.ErrorType
import ru.practicum.android.diploma.filters.domain.IndustryInteractor
import ru.practicum.android.diploma.filters.domain.model.Industry

class IndustryViewModel(private val interactor: IndustryInteractor) : ViewModel() {

    fun test() {
        interactor.getIndustries()
            .onEach { (data, error) ->
                processingResult(data, error)
            }.launchIn(viewModelScope)

    }

    private fun processingResult(industry: List<Industry>?, errorType: ErrorType?) {
        if (industry != null) {
            industry.forEach { Log.d("testList", "${it.id.toString()} ${it.name}") }
        } else {
            when (errorType) {
                ErrorType.NOTHING_FOUND -> {
                }

                else -> {}
            }
        }
    }
}
