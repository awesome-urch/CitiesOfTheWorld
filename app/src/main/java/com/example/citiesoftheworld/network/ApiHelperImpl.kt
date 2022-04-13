package com.example.citiesoftheworld.network

import com.example.citiesoftheworld.network.model.WorldCities
import retrofit2.Response

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {

//    override suspend fun getUsers(): Response<List<User>> = apiService.getUsers()

    override suspend fun getWorldCities(): Response<WorldCities> = apiService.getWorldCities()

//    override suspend fun getWorldCities(): Response<WorldCities> {
//        return apiService.getWorldCities()
//    }

}