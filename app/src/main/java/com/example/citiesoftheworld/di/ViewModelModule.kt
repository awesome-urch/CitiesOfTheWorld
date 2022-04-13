package com.example.citiesoftheworld.di

import com.example.citiesoftheworld.view.city.CitiesViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        CitiesViewModel(get())
    }
}