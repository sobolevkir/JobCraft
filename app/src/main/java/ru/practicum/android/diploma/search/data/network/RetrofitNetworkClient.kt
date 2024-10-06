package ru.practicum.android.diploma.search.data.network

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.common.ext.isNetworkConnected
import ru.practicum.android.diploma.search.data.network.dto.Response
import ru.practicum.android.diploma.search.data.network.dto.ResultCode

import ru.practicum.android.diploma.search.data.network.dto.VacanciesSearchRequest

class RetrofitNetworkClient(
    private val api: HHApi,
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        // Проверка соединения с интернетом
        if (!context.isNetworkConnected) {
            return Response().apply { resultCode = ResultCode.CONNECTION_PROBLEM }

        }
        Log.d("КЛИЕНТ", "подключение к сети есть")
        // Проверка типа запроса
        if (dto !is VacanciesSearchRequest) {
            return Response().apply { resultCode = ResultCode.BAD_REQUEST }
        }
        Log.d("КЛИЕНТ", "тип запроса корректный")
        // Выполнение запроса в IO-диспетчере
        return withContext(ioDispatcher) {
            try {
                Log.d("КЛИЕНТ", "выполняем запрос")
                val response = api.searchVacancies(dto.queryText, dto.page, 20)
                response.apply { resultCode = ResultCode.SUCCESS }  // Успешный результат
            } catch (ex: HttpException) {
                Log.d("КЛИЕНТ", "ошибка при выполнении запроса")
                // Обработка кода ошибки сервера
                Response().apply {
                    resultCode = when (ex.code()) {
                        ResultCode.NOTHING_FOUND -> ResultCode.NOTHING_FOUND  // Ничего не найдено
                        ResultCode.SERVER_ERROR -> ResultCode.SERVER_ERROR    // Ошибка сервера (500)
                        ResultCode.FORBIDDEN_ERROR -> ResultCode.FORBIDDEN_ERROR    // Ошибка сервера (500)
                        else -> ResultCode.UNKNOWN_ERROR                // Остальные ошибки
                    }
                }
            } catch (ex: Exception) {
                Log.d("КЛИЕНТ", "исключение при выполнении запроса")
                // Общая обработка исключений
                Response().apply { resultCode = ResultCode.UNKNOWN_ERROR }
            }
        }
    }
}
