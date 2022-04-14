package com.example.citiesoftheworld.view.city

import androidx.lifecycle.*
import com.example.citiesoftheworld.database.model.city.CityRepository
import com.example.citiesoftheworld.database.model.cityAndCountry.CityAndCountry
import com.example.citiesoftheworld.network.model.Items
import com.example.citiesoftheworld.network.model.WorldCitiesApiParams
import com.example.citiesoftheworld.network.model.WorldCitiesResultState
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

    fun setWorldCitiesApiParams(nameFilter: String){
        worldCitiesApiParams.nameFilter = nameFilter
    }

    val getWorldCitiesResultMutableLiveData = MutableLiveData<WorldCitiesApiParams>()
    @FlowPreview
    val getWorldCitiesResultLiveData: LiveData<WorldCitiesResultState> = getWorldCitiesResultMutableLiveData.switchMap {
        liveData {
            Timber.d(" getHomeFeedCloseUserStream")
            val repos = cityRepository.getWorldCitiesStream(it).asLiveData(
                Dispatchers.Main)
            emitSource(repos)
        }
    }

    fun cityListScrolled(visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
            Timber.d("get extra cities")
            viewModelScope.launch {
                Timber.d("request more")
                cityRepository.requestMore(worldCitiesApiParams)
//                chattedContactRepository.requestMore()
            }
        }
    }

    fun saveCloseShowrooms(itemList: List<Items>){
        cityRepository.saveCloseShowrooms(itemList)
    }

    fun getCitiesLiveData(): LiveData<MutableList<CityAndCountry>> {
        return cityRepository.getCitiesLiveData()
    }

    fun getCitiesByNameLiveData(name: String?): LiveData<MutableList<CityAndCountry>> {
        return cityRepository.getCitiesByNameLiveData(name)
    }

//    private val _users = MutableLiveData<Resource<List<User>>>()
//    val users: LiveData<Resource<List<User>>>
//        get() = _users
//
//    init {
//        fetchUsers()
//    }

//    private fun fetchUsers() {
//
//        viewModelScope.launch {
//
//            mainRepository.getAllUsers()
//
//            _users.postValue(Resource.loading(null))
//            if (networkHelper.isNetworkConnected()) {
//                mainRepository.getUsers().let {
//                    if (it.isSuccessful) {
//                        _users.postValue(Resource.success(it.body()))
//                    } else _users.postValue(Resource.error(it.errorBody().toString(), null))
//                }
//            } else _users.postValue(Resource.error("No internet connection", null))
//        }
//    }
}