package ru.practicum.android.diploma.filters.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.filters.domain.model.Area

class PlaceViewModel : ViewModel() {

    private var areaLiveData = MutableLiveData(PlaceState(null, null))

    fun getAreaLiveData(): LiveData<PlaceState> = areaLiveData

    fun passNewParameters(country: Area?, region: Area?) {
        areaLiveData.postValue(PlaceState(country?.name, region?.name))
    }
}

