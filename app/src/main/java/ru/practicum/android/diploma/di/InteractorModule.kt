package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.domain.FavoritesInteractor
import ru.practicum.android.diploma.favorites.domain.impl.FavoritesInteractorImpl
import ru.practicum.android.diploma.filters.domain.AreaInteractor
import ru.practicum.android.diploma.filters.domain.FiltersLocalInteractor
import ru.practicum.android.diploma.filters.domain.IndustryInteractor
import ru.practicum.android.diploma.filters.domain.impl.AreaInteractorImpl
import ru.practicum.android.diploma.filters.domain.impl.FiltersLocalInteractorImpl
import ru.practicum.android.diploma.filters.domain.impl.IndustryInteractorImpl
import ru.practicum.android.diploma.search.domain.VacanciesInteractor
import ru.practicum.android.diploma.search.domain.impl.VacanciesInteractorImpl
import ru.practicum.android.diploma.vacancy.domain.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.vacancy.domain.impl.VacancyDetailsInteractorImpl

val interactorModule = module {

    factory<VacanciesInteractor> { VacanciesInteractorImpl(repository = get()) }
    factory<FavoritesInteractor> { FavoritesInteractorImpl(repository = get()) }
    factory<VacancyDetailsInteractor> { VacancyDetailsInteractorImpl(repository = get()) }
    factory<IndustryInteractor> { IndustryInteractorImpl(repository = get()) }
    factory<AreaInteractor> { AreaInteractorImpl(repository = get()) }
    factory<FiltersLocalInteractor> { FiltersLocalInteractorImpl(repository = get()) }
}
