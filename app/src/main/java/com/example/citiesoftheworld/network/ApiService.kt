package com.example.citiesoftheworld.network

import com.example.citiesoftheworld.network.model.WorldCities
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("city")
    suspend fun getWorldCities(
        @Query("filter[0][name][contains]") filterName: String,
        @Query("include") include: String = "country",
        @Query("page") page: Int,
    ): Response<WorldCities>

    //https://connect-demo.mobile1.io/square1/connect/v1/city?filter[0][name][contains]=ni&page=2&include=country

}