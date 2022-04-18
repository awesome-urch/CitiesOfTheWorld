package com.example.citiesoftheworld.database.model.city

import com.example.citiesoftheworld.database.model.cityAndCountry.CityAndCountry

class CityRepositoryAndroidTest {

    var citiesData: LinkedHashMap<Long, CityAndCountry> = LinkedHashMap()

    fun testGetCitiesByNameLiveData() {}

    fun testSaveWorldCities(cityAndCountry: CityAndCountry) {
        citiesData[cityAndCountry.city.id] = cityAndCountry
    }
}