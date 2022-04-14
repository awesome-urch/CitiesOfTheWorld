package com.example.citiesoftheworld.network

import com.example.citiesoftheworld.network.model.WorldCities
import com.example.citiesoftheworld.network.model.WorldCitiesApiParams
import retrofit2.Response
import timber.log.Timber

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {

//    override suspend fun getUsers(): Response<List<User>> = apiService.getUsers()

    override suspend fun getWorldCities(worldCitiesApiParams: WorldCitiesApiParams): Response<WorldCities> {
        Timber.d("before************idkddkdk")
        val x = apiService.getWorldCities(
            filterName = worldCitiesApiParams.nameFilter,
            page = worldCitiesApiParams.page,
            include = worldCitiesApiParams.include
        )
        Timber.d("************idkddkdk")
        Timber.d("$x")
        return x
    }

//    override suspend fun getWorldCities(): Response<WorldCities> {
//        return apiService.getWorldCities()
//    }

}