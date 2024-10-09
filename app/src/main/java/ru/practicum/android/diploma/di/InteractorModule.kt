package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.domain.FavoritesInteractor
import ru.practicum.android.diploma.favorites.domain.impl.FavoritesInteractorImpl
import ru.practicum.android.diploma.search.domain.VacanciesInteractor
import ru.practicum.android.diploma.search.domain.impl.VacanciesInteractorImpl
import ru.practicum.android.diploma.vacancy.domain.VacancyDetailsInteractor
import ru.practicum.android.diploma.vacancy.domain.impl.VacancyDetailsInteractorImpl

val interactorModule = module {

    factory<VacanciesInteractor> { VacanciesInteractorImpl(repository = get()) }
    factory<FavoritesInteractor> { FavoritesInteractorImpl(repository = get()) }

    factory<VacancyDetailsInteractor> { VacancyDetailsInteractorImpl(repository = get()) }

}
