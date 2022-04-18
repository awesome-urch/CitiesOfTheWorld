package com.example.citiesoftheworld.di

import com.example.citiesoftheworld.database.model.city.CityRepository
import com.example.citiesoftheworld.database.model.city.DefaultCityRepository
import org.koin.dsl.binds
import org.koin.dsl.module

val repoModule = module {

    single { DefaultCityRepository(get(),get(), get()) } binds arrayOf(CityRepository::class)

}