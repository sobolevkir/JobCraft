package ru.practicum.android.diploma.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.practicum.android.diploma.search.data.impl.VacanciesRepositoryImpl
import ru.practicum.android.diploma.search.domain.VacanciesRepository
import ru.practicum.android.diploma.vacancy.data.impl.VacancyDetailsRepositoryImpl
import ru.practicum.android.diploma.vacancy.domain.VacancyDetailsRepository

val repositoryModule = module {

    single<VacanciesRepository> {
        VacanciesRepositoryImpl(
            networkClient = get(),
            ioDispatcher = get(named("ioDispatcher"))
        )
    }

    single<VacancyDetailsRepository> {
        VacancyDetailsRepositoryImpl(
            networkClient = get(),
            ioDispatcher = get(named("ioDispatcher"))
        )
    }

}
