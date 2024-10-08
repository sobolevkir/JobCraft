package ru.practicum.android.diploma.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.practicum.android.diploma.vacancy.data.impl.VacancyDetailsRepositoryImpl
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsRepository

val repositoryModule = module {

    single<VacancyDetailsRepository> {
        VacancyDetailsRepositoryImpl(
            externalNavigator = get (),
            appDatabase = get (),
            dbConvertor = get(),
            networkClient = get(),
            ioDispatcher = get(named("ioDispatcher")),
            parametersConverter = get()
        )
    }

}
