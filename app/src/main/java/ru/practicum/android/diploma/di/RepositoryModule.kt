package ru.practicum.android.diploma.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.data.impl.FavoritesRepositoryImpl
import ru.practicum.android.diploma.favorites.domain.FavoritesRepository
import ru.practicum.android.diploma.filters.data.impl.AreaRepositoryImpl
import ru.practicum.android.diploma.filters.data.impl.IndustryRepositoryImpl
import ru.practicum.android.diploma.filters.domain.AreaRepository
import ru.practicum.android.diploma.filters.domain.IndustryRepository
import ru.practicum.android.diploma.search.data.impl.VacanciesRepositoryImpl
import ru.practicum.android.diploma.search.domain.VacanciesRepository
import ru.practicum.android.diploma.vacancy.data.impl.VacancyDetailsRepositoryImpl
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsRepository

private const val IO_DISPATCHER = "ioDispatcher"

val repositoryModule = module {

    single<VacanciesRepository> {
        VacanciesRepositoryImpl(
            networkClient = get(),
            ioDispatcher = get(named(IO_DISPATCHER)),
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
            ioDispatcher = get(named(IO_DISPATCHER)),
            parametersConverter = get()
        )
    }

    single<IndustryRepository> {
        IndustryRepositoryImpl(
            networkClient = get(),
            ioDispatcher = get(named(IO_DISPATCHER)),
        )
    }

    single<AreaRepository> {
        AreaRepositoryImpl(
            networkClient = get(),
            ioDispatcher = get(named(IO_DISPATCHER)),
        )
    }

}
