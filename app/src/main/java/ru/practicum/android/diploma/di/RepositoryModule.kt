package ru.practicum.android.diploma.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.data.impl.FavoritesRepositoryImpl
import ru.practicum.android.diploma.favorites.domain.FavoritesRepository
import ru.practicum.android.diploma.search.data.impl.VacanciesRepositoryImpl
import ru.practicum.android.diploma.search.domain.VacanciesRepository
import ru.practicum.android.diploma.vacancy.data.impl.VacancyDetailsRepositoryImpl
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsRepository

val repositoryModule = module {

    single<VacanciesRepository> {
        VacanciesRepositoryImpl(
            networkClient = get(),
            ioDispatcher = get(named("ioDispatcher")),
            parametersConverter = get()
        )
    }
    single<FavoritesRepository> {
        FavoritesRepositoryImpl(
            appDatabase = get(),
            vacancyDbConverter = get()
        )
    }

    single<VacancyDetailsRepository> {
        VacancyDetailsRepositoryImpl(
            externalNavigator = get(),
            appDatabase = get(),
            dbConverter = get(),
            networkClient = get(),
            ioDispatcher = get(named("ioDispatcher")),
            parametersConverter = get()
        )
    }

}
