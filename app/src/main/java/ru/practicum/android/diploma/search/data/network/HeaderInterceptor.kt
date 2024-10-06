package ru.practicum.android.diploma.search.data.network

import okhttp3.Interceptor
import okhttp3.Response
import ru.practicum.android.diploma.BuildConfig

class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val request = originalRequest
            .newBuilder()
            .header("Authorization", "Bearer ${BuildConfig.HH_ACCESS_TOKEN}")
            .header("HH-User-Agent", USER_AGENT)
            .build()

        return chain.proceed(request)
    }

    companion object {
        private const val USER_AGENT = "JobCraft (sobolevkir@bk.ru)"
    }
}
