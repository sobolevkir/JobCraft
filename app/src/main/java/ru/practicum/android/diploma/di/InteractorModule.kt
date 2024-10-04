package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.domain.FavoritesInteractor
import ru.practicum.android.diploma.favorites.domain.impl.FavoritesInteractorImpl

val interactorModule = module {
    factory<FavoritesInteractor> { FavoritesInteractorImpl(get()) }
}
