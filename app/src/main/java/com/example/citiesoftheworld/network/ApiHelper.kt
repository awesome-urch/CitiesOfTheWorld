package com.example.citiesoftheworld.network

import com.example.citiesoftheworld.network.model.WorldCities
import com.example.citiesoftheworld.network.model.WorldCitiesApiParams
import retrofit2.Response

interface ApiHelper {

    suspend fun getWorldCities(worldCitiesApiParams: WorldCitiesApiParams): Response<WorldCities>
}