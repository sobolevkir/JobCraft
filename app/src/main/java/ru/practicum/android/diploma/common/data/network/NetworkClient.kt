package ru.practicum.android.diploma.common.data.network

import ru.practicum.android.diploma.common.data.network.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}
