package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.data.converter.FavoriteVacancyDbConverter
import ru.practicum.android.diploma.favorites.data.impl.FavoritesRepositoryImpl
import ru.practicum.android.diploma.favorites.domain.FavoritesRepository

val repositoryModule = module {
    factory<FavoritesRepository> { FavoritesRepositoryImpl(get(), get()) }
    single { FavoriteVacancyDbConverter() }
}
