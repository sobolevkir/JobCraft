package ru.practicum.android.diploma.di

import androidx.room.Room
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.common.data.converter.FavoriteVacancyDbConverter
import ru.practicum.android.diploma.common.data.converter.ParametersConverter
import ru.practicum.android.diploma.common.data.db.AppDatabase
import ru.practicum.android.diploma.common.data.network.HHApi
import ru.practicum.android.diploma.common.data.network.HeaderInterceptor
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.vacancy.data.impl.ExternalNavigatorImpl
import ru.practicum.android.diploma.vacancy.domain.ExternalNavigator

private const val BASE_URL = "https://api.hh.ru/"

val dataModule = module {

    single<HHApi> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HHApi::class.java)
    }

    single {
        Gson()
    }

    factory { ParametersConverter(context = get()) }
    factory { FavoriteVacancyDbConverter(get()) }

    single<NetworkClient> {
        RetrofitNetworkClient(api = get(), context = androidContext(), ioDispatcher = get(named("ioDispatcher")))
    }

    single<CoroutineDispatcher>(named("ioDispatcher")) {
        Dispatchers.IO
    }

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor())
            .build()
    }
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }
}
