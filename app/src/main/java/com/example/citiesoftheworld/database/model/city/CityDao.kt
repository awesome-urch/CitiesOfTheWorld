package com.example.citiesoftheworld.database.model.city

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.citiesoftheworld.database.model.cityAndCountry.CityAndCountry

@Dao
interface CityDao {

    @Query("SELECT * FROM city ")
    fun getCitiesLiveData(): LiveData<MutableList<CityAndCountry>>

//    @Query("SELECT * FROM city WHERE name LIKE :name OR local_name LIKE :name ")
//    fun getCitiesByNameLiveData(name: String?): LiveData<MutableList<CityAndCountry>>

    @Query("SELECT * FROM city WHERE name LIKE :name OR local_name LIKE :name ")
    fun getCitiesByNameLiveData(name: String?): LiveData<MutableList<CityAndCountry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city: City)


}
