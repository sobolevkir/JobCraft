package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.presentation.FavoritesViewModel
import ru.practicum.android.diploma.filters.presentation.CountryViewModel
import ru.practicum.android.diploma.filters.presentation.IndustryConverter
import ru.practicum.android.diploma.filters.presentation.IndustryViewModel
import ru.practicum.android.diploma.filters.presentation.RegionViewModel
import ru.practicum.android.diploma.search.presentation.SearchViewModel
import ru.practicum.android.diploma.vacancy.presentation.VacancyViewModel

val viewModelModule = module {

    viewModel {
        SearchViewModel(vacanciesInteractor = get(), filtersLocalInteractor = get())
    }

    viewModel { params ->
        VacancyViewModel(params.get(), interactor = get())
    }

    viewModel {
        FavoritesViewModel(interactor = get())
    }

    viewModel {
        IndustryViewModel(interactor = get(), converter = get())
    }

    viewModel {
        RegionViewModel(interactor = get())
    }

    viewModel {
        CountryViewModel(interactor = get())
    }

    factory { IndustryConverter() }
}
