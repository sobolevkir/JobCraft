package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.search.domain.VacanciesInteractor
import ru.practicum.android.diploma.search.domain.impl.VacanciesInteractorImpl

val interactorModule = module {

    factory<VacanciesInteractor> { VacanciesInteractorImpl(repository = get()) }

}
