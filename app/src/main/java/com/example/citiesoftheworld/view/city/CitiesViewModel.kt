package com.example.citiesoftheworld.view.city

import androidx.lifecycle.*
import com.example.citiesoftheworld.database.model.city.CityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import timber.log.Timber

class CitiesViewModel(
    private val cityRepository: CityRepository,
) : ViewModel() {


    val getCloseUserHomeFeedLiveData = MutableLiveData<Unit>()
    @FlowPreview
    val getCloseUserHomeFeedResult: LiveData<CityRepository.WorldCityUiState> = getCloseUserHomeFeedLiveData.switchMap {
        liveData {
            Timber.d(" getHomeFeedCloseUserStream")
            val repos = cityRepository.getWorldCitiesStream().asLiveData(
                Dispatchers.Main)
            emitSource(repos)
        }
    }

    val check = "Check it"

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