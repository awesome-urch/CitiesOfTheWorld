package com.example.citiesoftheworld.database.model.city

import androidx.lifecycle.LiveData
import com.example.citiesoftheworld.database.model.cityAndCountry.CityAndCountry
import com.example.citiesoftheworld.database.model.country.Country
import com.example.citiesoftheworld.database.model.country.CountryDao
import com.example.citiesoftheworld.network.ApiHelper
import com.example.citiesoftheworld.network.model.Items
import com.example.citiesoftheworld.network.model.WorldCitiesApiParams
import com.example.citiesoftheworld.network.model.WorldCitiesResultState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class CityRepository (
    private val apiHelper: ApiHelper,
    private val cityDao: CityDao,
    private val countryDao: CountryDao
    ) {

    private val worldCitiesResult = MutableStateFlow<WorldCitiesResultState>(WorldCitiesResultState.Empty)
    private val worldCitiesResultState: StateFlow<WorldCitiesResultState> = worldCitiesResult

    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage = 1

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    fun getCitiesLiveData(): LiveData<MutableList<CityAndCountry>> {
        return cityDao.getCitiesLiveData()
    }

    fun getCitiesByNameLiveData(name: String?): LiveData<MutableList<CityAndCountry>> {
        return cityDao.getCitiesByNameLiveData(name)
    }

    @FlowPreview
    suspend fun getWorldCitiesStream(worldCitiesApiParams: WorldCitiesApiParams): Flow<WorldCitiesResultState> {
        requestWorldCities(worldCitiesApiParams)
        return worldCitiesResultState
    }

    private suspend fun requestWorldCities(worldCitiesApiParams: WorldCitiesApiParams): Boolean {
        isRequestInProgress = true
        val successful = false

        Timber.tag("DEBUG_HF").d("last page $lastRequestedPage")

        try {

            Timber.tag("DEBUG_HF").d("params $worldCitiesApiParams")

            val response = apiHelper.getWorldCities(
                worldCitiesApiParams
            )

            if (response.isSuccessful) {
                response.body()?.let { worldCities ->
                    withContext(Dispatchers.IO) {

                        Timber.tag("TIMEBUWO").d("$worldCities")

//                        worldCityUiStateResult.offer(WorldCityUiState.Success)
                        val data = worldCities.data
                        if(data != null){
                            val items = data.items
                            if(items != null){
                                worldCitiesResult.value = WorldCitiesResultState.Success(items)
                            }else{
                                worldCitiesResult.value = WorldCitiesResultState.Error("")
                            }
                        }else{
                            worldCitiesResult.value = WorldCitiesResultState.Error("")
                        }

                        Timber.d("data is $worldCities")

                    }
                }
            }else{
//                worldCityUiStateResult.offer(WorldCityUiState.Error(""))
                worldCitiesResult.value = WorldCitiesResultState.Error("")

            }

        }catch (exception: IOException) {
            Timber.tag("DEBUG_HF").d("exception $exception")

            worldCitiesResult.value = WorldCitiesResultState.Error("$exception")

        } catch (exception: HttpException) {
            Timber.tag("DEBUG_HF").d("exception $exception")

            worldCitiesResult.value = WorldCitiesResultState.Error("")

        }

        isRequestInProgress = false
        return successful
    }

    suspend fun requestMore(worldCitiesApiParams: WorldCitiesApiParams) {
        if (isRequestInProgress) return
        lastRequestedPage++
        worldCitiesApiParams.page = lastRequestedPage
        requestWorldCities(worldCitiesApiParams)
    }

    fun saveCloseShowrooms(itemList: List<Items>){
        CoroutineScope(Dispatchers.IO).launch {
            for(city in itemList){

                Timber.tag("DEBUG_HF").d("city is $city")

                Timber.d("${city.id} has ${city.name}km")

                city.id?.let { cityId ->
                    City(
                        id = cityId.toLong(),
                        name = city.name,
                        localName = city.local_name,
                        lat = city.lat,
                        lng = city.lng,
                        countryId = city.country_id?.toLong()
                    )
                }?.let { cityModel ->
                    cityDao.insert(
                        cityModel
                    )
                }

                val country = city.country

                if(country != null){
                    country.id?.let { countryId ->
                        Country(
                            id = countryId.toLong(),
                            name = country.name,
                            code = country.code,
                            continentId = country.continent_id
                        )
                    }?.let { countryModel ->
                        countryDao.insert(
                            countryModel
                        )
                    }
                }
            }
        }
    }




}