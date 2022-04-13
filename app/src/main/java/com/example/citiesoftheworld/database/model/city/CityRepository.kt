package com.example.citiesoftheworld.database.model.city

import android.accounts.NetworkErrorException
import android.util.Log
import com.example.citiesoftheworld.database.model.country.CountryDao
import com.example.citiesoftheworld.network.ApiHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class CityRepository (
    private val apiHelper: ApiHelper,
    private val cityDao: CityDao,
    private val countryDao: CountryDao
    ) {

//    lateinit var list:Response<List<User>>
//    lateinit var listdata:ArrayList<User>

//    suspend fun getUsers() =  apiHelper.getUsers()

//    @OptIn(ObsoleteCoroutinesApi::class)
    private val worldCityUiStateResult = ConflatedBroadcastChannel<WorldCityUiState>()

    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage = 1

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false


    @FlowPreview
    suspend fun getWorldCitiesStream(): Flow<WorldCityUiState> {
        Timber.d("request in progress getHomeFeedCloseUserStream")
        requestAndSaveData()
        return worldCityUiStateResult.asFlow()
    }


    private suspend fun requestAndSaveData(): Boolean {
        isRequestInProgress = true
        var successful = false

        Timber.tag("DEBUG_HF").d("last page $lastRequestedPage")


        try {
//            var batchSize = PAGE_BATCH_SIZE
//            if(lastRequestedPage == 2) batchSize = FIRST_PAGE_BATCH_SIZE
//            val response = myShowroomApi.getHomeFeedShowroom(
//                request = HOME_FEED_SHOWROOM,
//                lat = userRepository.getOwner().latitude.toString(),
//                lng = userRepository.getOwner().longitude.toString(),
//                search = searchBusinessInput.search,
//                start = (lastRequestedPage - 1).times(batchSize),
//                viewer = userRepository.getOwner().userRemoteId.toString(),
//                businessOwner = "0",
//                key = KEY)

            val response = apiHelper.getWorldCities()

//            Timber.tag("DEBUG_HF").d("batch size $PAGE_BATCH_SIZE")

            if (response.isSuccessful) {
                response.body()?.let { worldCities ->
                    withContext(Dispatchers.IO) {

                        Timber.tag("TIMEBUWO").d("${worldCities.time}")

                        worldCityUiStateResult.offer(WorldCityUiState.Success)

                        Timber.d(worldCities.toString())

//                        listdata.addAll(userList)
                    }
                }
            }else{
                worldCityUiStateResult.offer(WorldCityUiState.Error(""))
            }
            
            
//            if(response.error == 0){
//
//                val data = response.data as MutableList<HomeFeedShowroomData>
//
//                if(lastRequestedPage == 1){
//                    Timber.tag("DEBUG_HF").d("first page, refresh data")
//                    if(data.isNotEmpty()){
//                        closeBusinessesHomeFeedDao.deleteAll()
//                    }
////                    requestBusinessOwnerShowroom()
////                    var userShowroomList = getBusinessOwnerShowroom()
//                    data.addAll(getBusinessOwnerShowroom())
//                }
//
//                Timber.tag("DEBUG_HF").d("data is $data")
//
////                if(lastRequestedPage == 1){
////                    worldCityUiStateResult.offer(HomeFeedCloseUserResult.SuccessfullyFetchedFirstPage)
////                }
//                Timber.tag("RFRSH").d("SuccessfullyGotFeedShowroom repo")
//                worldCityUiStateResult.offer(HomeFeedCloseUserResult.SuccessfullyGotFeedShowroom(data))
//                successful = true
//
//            }else{
//                Timber.tag("DEBUG_HF").d("error from server is $response")
//                worldCityUiStateResult.offer(WorldCityUiState.Error(""))
////                if(lastRequestedPage == 1){
////                    worldCityUiStateResult.offer(HomeFeedCloseUserResult.ErrorFetchingFirstPage)
////                }
//            }

        }catch (exception: IOException) {
            Timber.tag("DEBUG_HF").d("exception $exception")
//            worldCityUiStateResult.offer(HomeFeedCloseUserResult.Error(exception))
            worldCityUiStateResult.offer(WorldCityUiState.Error("$exception"))
//            if(lastRequestedPage == 1){
//                Timber.tag("DEBUG_HF").d("ErrorFetchingFirstPage $exception")
//                worldCityUiStateResult.offer(HomeFeedCloseUserResult.ErrorFetchingFirstPage)
//            }
        } catch (exception: HttpException) {
            Timber.tag("DEBUG_HF").d("exception $exception")
//            worldCityUiStateResult.offer(HomeFeedCloseUserResult.Error(exception))
            worldCityUiStateResult.offer(WorldCityUiState.Error(""))
//            if(lastRequestedPage == 1){
//                Timber.tag("DEBUG_HF").d("ErrorFetchingFirstPage $exception")
//                worldCityUiStateResult.offer(HomeFeedCloseUserResult.ErrorFetchingFirstPage)
//            }
        }

        isRequestInProgress = false
        return successful
    }

//    suspend fun getAllUsers() { //: Response<List<User>>
//        val responseGet = apiHelper.getUsers()
//        try {
//            if (responseGet.isSuccessful) {
//                responseGet.body()?.let { userList ->
//                    withContext(Dispatchers.IO) {
//
//                        Log.d("MAINREPO",userList.toString())
//
//                        for(user in userList){
//                            userDao.addUser(
//                                UserModel(
//                                    0,
//                                    user.name,
//                                    user.email,
//                                    user.avatar
//                                )
//                            )
//                        }
//
////                        listdata.addAll(userList)
//                    }
//                }
//            }
//        } catch (e: NetworkErrorException) {
//
//        }
//
//    }

    sealed class WorldCityUiState {
        object Success : WorldCityUiState()
        data class Error(val message: String) : WorldCityUiState()
        object Loading : WorldCityUiState()
        object Empty : WorldCityUiState()
    }

}