package ru.practicum.android.diploma.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.search.domain.SearchInteractor

class SearchViewModel(private val interactor: SearchInteractor) : ViewModel() {

    private val liveDataSearchRes = MutableLiveData<SearchLiveDataObject>()

    fun getSearchRes(): LiveData<SearchLiveDataObject> = liveDataSearchRes

    fun search() {

    }
}
