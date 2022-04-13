package com.example.citiesoftheworld.di

import com.example.citiesoftheworld.database.model.city.CityRepository
import org.koin.dsl.module

val repoModule = module {
    single {
        CityRepository(get(),get(), get())
    }
}