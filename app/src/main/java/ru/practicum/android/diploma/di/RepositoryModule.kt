package ru.practicum.android.diploma.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.practicum.android.diploma.common.data.db.dao.VacancyDBConverter
import ru.practicum.android.diploma.search.data.impl.VacanciesRepositoryImpl
import ru.practicum.android.diploma.search.domain.VacanciesRepository

val repositoryModule = module {

    factory { VacancyDBConverter() }

    single<VacanciesRepository> {
        VacanciesRepositoryImpl(
            networkClient = get(),
            ioDispatcher = get(named("ioDispatcher"))
        )
    }

}
