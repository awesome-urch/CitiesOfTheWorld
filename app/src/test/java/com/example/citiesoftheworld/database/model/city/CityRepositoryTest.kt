package com.example.citiesoftheworld.database.model.city

//import com.example.citiesoftheworld.network.model.Country
import androidx.lifecycle.MutableLiveData
import com.example.citiesoftheworld.database.model.cityAndCountry.CityAndCountry
import com.example.citiesoftheworld.database.model.country.Country
import com.example.citiesoftheworld.network.model.Items
import com.example.citiesoftheworld.network.model.WorldCitiesApiParams
import com.example.citiesoftheworld.network.model.WorldCitiesResultState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

class CityRepositoryTest: CityRepository {

    private val cityList = mutableListOf<CityAndCountry>()

    private val observableCityList = MutableLiveData(cityList)

    private fun refreshLiveData() {
        observableCityList.postValue(cityList)
    }

    override fun getCitiesByNameLiveData(name: String?): MutableLiveData<MutableList<CityAndCountry>> {
        return observableCityList
    }

    @FlowPreview
    override suspend fun getWorldCitiesStream(worldCitiesApiParams: WorldCitiesApiParams): Flow<WorldCitiesResultState> {
        TODO("Not yet implemented")
    }

    override suspend fun requestWorldCities(worldCitiesApiParams: WorldCitiesApiParams): Boolean {
        return true
    }

    override suspend fun requestMore(worldCitiesApiParams: WorldCitiesApiParams) {

    }

    override fun saveWorldCities(itemList: List<Items>) {
        for(cityItem in itemList){
            val city = cityItem.id?.let {
                City(
                    id = it.toLong(),
                    name = cityItem.name,
                    localName = cityItem.local_name,
                    lat = cityItem.lat,
                    lng = cityItem.lng,
                    countryId = cityItem.country_id?.toLong()
                )
            }

            val country = cityItem.country?.id?.let {
                Country(
                    id = it.toLong(),
                    name = cityItem.country?.name,
                    code = cityItem.country?.code,
                    continentId = cityItem.country?.continent_id
                )
            }

            val cityAndCountry = city?.let {
                CityAndCountry(
                    city = it,
                    country = country
                )
            }

            if (cityAndCountry != null) {
                cityList.add(cityAndCountry)
            }

        }
        refreshLiveData()
    }

}