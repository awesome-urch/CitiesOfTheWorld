package com.example.citiesoftheworld.network

import com.example.citiesoftheworld.network.model.WorldCities
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("city")
    suspend fun getWorldCities(): Response<WorldCities>

}