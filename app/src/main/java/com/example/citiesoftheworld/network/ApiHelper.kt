package com.example.citiesoftheworld.network

import com.example.citiesoftheworld.network.model.WorldCities
import retrofit2.Response

interface ApiHelper {

    suspend fun getWorldCities(): Response<WorldCities>
}