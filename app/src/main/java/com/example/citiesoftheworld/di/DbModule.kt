package com.example.citiesoftheworld.di

import android.app.Application
import androidx.room.Room
import com.example.citiesoftheworld.database.AppDatabase
import com.example.citiesoftheworld.database.model.city.CityDao
import com.example.citiesoftheworld.database.model.country.CountryDao
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dbModule = module {
    fun provideDataBase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "application_db")
            .build()
    }

    fun provideCityDao(dataBase: AppDatabase): CityDao {
        return dataBase.cityDao
    }

    fun provideCountryDao(dataBase: AppDatabase): CountryDao {
        return dataBase.countryDao
    }

    single { provideDataBase(androidApplication()) }
    single { provideCityDao(get()) }
    single { provideCountryDao(get()) }

}