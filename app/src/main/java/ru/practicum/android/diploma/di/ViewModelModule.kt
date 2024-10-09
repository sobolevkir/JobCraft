package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.presentation.FavoritesViewModel
import ru.practicum.android.diploma.search.presentation.SearchViewModel
import ru.practicum.android.diploma.vacancy.presentation.VacancyViewModel

val viewModelModule = module {

    viewModel { SearchViewModel(vacanciesInteractor = get()) }
    viewModel { FavoritesViewModel(favoritesInteractor = get()) }

    viewModel { VacancyViewModel(interactor = get()) }

}
