package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.search.presentation.SearchViewModel

val viewModelModule = module {

    single {
        SearchViewModel(interactor = get())
    }
}
