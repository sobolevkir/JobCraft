package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.search.presentation.SearchViewModel

val viewModelModule = module {

    viewModel { SearchViewModel(vacanciesInteractor = get()) }
    viewModel { FavoritesViewModel(favoritesInteractor = get()) }

    viewModel { VacancyViewModel(interactor = get()) }

    single {
        SearchViewModel(interactor = get())
    }
}
