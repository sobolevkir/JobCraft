package ru.practicum.android.diploma.filters.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.practicum.android.diploma.common.data.network.dto.AreaFilterDto
import ru.practicum.android.diploma.common.domain.model.ErrorType
import ru.practicum.android.diploma.filters.domain.AreaInteractor
import ru.practicum.android.diploma.filters.domain.IndustryInteractor
import ru.practicum.android.diploma.filters.domain.model.AreaFilter
import ru.practicum.android.diploma.filters.domain.model.Industry

class AreaViewModel(private val interactor: AreaInteractor) : ViewModel() {

    fun test() {
        interactor.getAreas()
            .onEach { (data, error) ->
                processingResult(data, error)
            }.launchIn(viewModelScope)

    }

    private fun processingResult(area: List<AreaFilter>?, errorType: ErrorType?) {
        if (area != null) {
            area.forEach { Log.d("testList", "${it.id} ${it.name} ${showAreas(area)}") }
        } else {
            when (errorType) {
                ErrorType.NOTHING_FOUND -> {
                    Log.d("testList", "error")
                }

                else -> {
                    Log.d("testList", "error")}
            }
        }
    }
    private fun showAreas(area: List<AreaFilter>): String {
        val stringBuilder = StringBuilder()
        area.forEach {
            if (it.parentId == null) {
                stringBuilder.append(it.name).append("\n")
                appendAreas(stringBuilder, convertArea(it.areas))
            }
        }
        return stringBuilder.toString()
    }

    private fun appendAreas(string: StringBuilder, areas: List<AreaFilter>?) {
        areas?.forEach {
            string.append(it.name + "\n")
            appendAreas(string, convertArea(it.areas))
        }
    }
    fun convertArea(areaList: List<AreaFilterDto>?): List<AreaFilter> {
        if (areaList != null) {
            return areaList.map {
                AreaFilter(it.id, it.parentId, it.name, it.areas)
            }
        }else return emptyList()
    }
}
