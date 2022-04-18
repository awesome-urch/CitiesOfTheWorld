package com.example.citiesoftheworld.database.model.city

import androidx.lifecycle.LiveData
import com.example.citiesoftheworld.database.model.cityAndCountry.CityAndCountry
import com.example.citiesoftheworld.network.model.Items
import com.example.citiesoftheworld.network.model.WorldCitiesApiParams
import com.example.citiesoftheworld.network.model.WorldCitiesResultState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

interface CityRepository {
    fun getCitiesByNameLiveData(name: String?): LiveData<MutableList<CityAndCountry>>

    @FlowPreview
    suspend fun getWorldCitiesStream(worldCitiesApiParams: WorldCitiesApiParams): Flow<WorldCitiesResultState>

    suspend fun requestWorldCities(worldCitiesApiParams: WorldCitiesApiParams): Boolean

    suspend fun requestMore(worldCitiesApiParams: WorldCitiesApiParams)
    fun saveWorldCities(itemList: List<Items>)
}