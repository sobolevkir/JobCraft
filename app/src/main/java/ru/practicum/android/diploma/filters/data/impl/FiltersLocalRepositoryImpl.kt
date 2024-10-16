package ru.practicum.android.diploma.filters.data.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.filters.domain.FiltersLocalRepository
import ru.practicum.android.diploma.filters.domain.model.FilterParameters

class FiltersLocalRepositoryImpl(
    private val gson: Gson,
    private val sharedPreferences: SharedPreferences
) : FiltersLocalRepository {

    override fun saveFilterParameters(filterParameters: FilterParameters) {
        val editor = sharedPreferences.edit()
        val jsonFilterParameters = gson.toJson(filterParameters)
        editor.putString(FILTER_PARAMETERS, jsonFilterParameters)
        editor.apply()
    }

    override fun getFilterParameters(): FilterParameters? {
        val jsonFilterParameters = sharedPreferences.getString(FILTER_PARAMETERS, null)
        return if (jsonFilterParameters != null) {
            val type = object : TypeToken<FilterParameters>() {}.type
            gson.fromJson(jsonFilterParameters, type)
        } else {
            null
        }
    }

    override fun clearFilters() {
        val editor = sharedPreferences.edit()
        editor.remove(FILTER_PARAMETERS)
        editor.apply()
    }

    companion object {
        private const val FILTER_PARAMETERS = "filter_parameters"
    }
}
