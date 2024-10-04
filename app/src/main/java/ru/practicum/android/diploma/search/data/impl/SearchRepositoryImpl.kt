package ru.practicum.android.diploma.search.data.impl

import ru.practicum.android.diploma.search.data.network.NetworkClient
import ru.practicum.android.diploma.search.domain.SearchRepository

class SearchRepositoryImpl(private val network : NetworkClient) : SearchRepository {
    suspend fun doRequest(){

    }
}
