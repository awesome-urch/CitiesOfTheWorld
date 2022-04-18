package com.example.citiesoftheworld.view.city

import androidx.lifecycle.*
import com.example.citiesoftheworld.database.model.city.CityRepository
import com.example.citiesoftheworld.database.model.cityAndCountry.CityAndCountry
import com.example.citiesoftheworld.network.model.Items
import com.example.citiesoftheworld.network.model.WorldCitiesApiParams
import com.example.citiesoftheworld.network.model.WorldCitiesResultState
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import timber.log.Timber


class CitiesViewModel(
    private val cityRepository: CityRepository,
) : ViewModel() {

    companion object {
        private const val VISIBLE_THRESHOLD = 5
    }

    var worldCitiesApiParams = WorldCitiesApiParams(nameFilter = "", page = 1)

    var defaultLatLng = LatLng(-27.47093, 153.0235)

    var searchByLiveData: LiveData<MutableList<CityAndCountry>>
    private val _filterLiveData = MutableLiveData<String>()
    val filterLiveData : LiveData<String> = _filterLiveData

    private val _mapViewVisible = MutableLiveData<Boolean>()
    val mapViewVisible: LiveData<Boolean> = _mapViewVisible

    init {

        searchByLiveData = Transformations.switchMap(
            _filterLiveData
        ) { name: String? ->
            cityRepository.getCitiesByNameLiveData(
                name
            )
        }

    }

    val getWorldCitiesResultMutableLiveData = MutableLiveData<WorldCitiesApiParams>()
    @FlowPreview
    val getWorldCitiesResultLiveData: LiveData<WorldCitiesResultState> = getWorldCitiesResultMutableLiveData.switchMap {
        liveData {
            val repos = cityRepository.getWorldCitiesStream(it).asLiveData(
                Dispatchers.Main)
            emitSource(repos)
        }
    }

    fun cityListScrolled(visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
            Timber.d("visible item : $visibleItemCount. lastVisible : $lastVisibleItemPosition. totalItem: $totalItemCount")
            viewModelScope.launch {
                Timber.d("request more")
                cityRepository.requestMore(worldCitiesApiParams)
//                chattedContactRepository.requestMore()
            }
        }
    }

    fun saveWorldCities(itemList: List<Items>){
        cityRepository.saveWorldCities(itemList)
    }

    fun setFilter(filter: String) {
        _filterLiveData.value = filter
        worldCitiesApiParams.nameFilter = filter
        getWorldCitiesResultMutableLiveData.postValue(worldCitiesApiParams)
    }

    fun setMapViewVisible(visible: Boolean){
        _mapViewVisible.value = visible
    }

}