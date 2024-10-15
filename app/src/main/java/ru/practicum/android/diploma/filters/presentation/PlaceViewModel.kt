package ru.practicum.android.diploma.filters.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.filters.domain.model.Area

class PlaceViewModel : ViewModel() {

    private var currentCountry: Area? = null
    private var currentRegion: Area? = null
    private var areaLiveData = MutableLiveData(AreaState(null, null))

    fun getAreaLiveData(): LiveData<AreaState> = areaLiveData

    fun passNewParameters(country: Area?, region: Area?) {
        var strCountry: String? = null
        var strRegion: String? = null
        if (country != currentCountry) {
            currentCountry = country
            strCountry = country?.name
        }
        if (region != currentRegion) {
            currentRegion = region
            strRegion = region?.name
        }
        areaLiveData.postValue(AreaState(strCountry, strRegion))
    }

    fun resetCountry() {
        if (currentCountry != null) {
            currentCountry = null
            areaLiveData.postValue(AreaState(null, currentRegion?.name))
        }
    }

    fun resetRegion() {
        if (currentRegion != null) {
            currentRegion = null
            areaLiveData.postValue(AreaState(currentCountry?.name, null))
        }
    }
}

data class AreaState (
    var country: String?,
    var region: String?
)
