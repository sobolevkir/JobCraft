package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.presentation.FavoritesViewModel
import ru.practicum.android.diploma.search.presentation.SearchViewModel
import ru.practicum.android.diploma.vacancy.presentation.VacancyViewModel

val viewModelModule = module {

    single {
        SearchViewModel(interactor = get())
    }

    single {
        VacancyViewModel(interactor = get())
    }

    single {
        FavoritesViewModel(interactor = get())
    }
}
