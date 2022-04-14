package com.example.citiesoftheworld.database.model.cityAndCountry

import androidx.room.Embedded
import androidx.room.Relation
import com.example.citiesoftheworld.database.model.city.City
import com.example.citiesoftheworld.database.model.country.Country

data class CityAndCountry(
    @Embedded val city: City,
    @Relation(
        parentColumn = "country_id",
        entityColumn = "id"
    )
    val country: Country?
)
