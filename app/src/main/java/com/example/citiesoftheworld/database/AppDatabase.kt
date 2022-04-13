package com.example.citiesoftheworld.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.citiesoftheworld.database.model.city.City
import com.example.citiesoftheworld.database.model.city.CityDao
import com.example.citiesoftheworld.database.model.country.Country
import com.example.citiesoftheworld.database.model.country.CountryDao

@Database(entities = [City::class,Country::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val cityDao: CityDao
    abstract val countryDao: CountryDao
}