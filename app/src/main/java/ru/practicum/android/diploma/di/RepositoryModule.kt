package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.search.data.impl.SearchRepositoryImpl
import ru.practicum.android.diploma.search.domain.SearchRepository

val repositoryModule = module {
    single<SearchRepository> {
        SearchRepositoryImpl(network = get())
    }
}
