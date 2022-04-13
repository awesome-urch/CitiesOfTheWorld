package com.example.citiesoftheworld.database.model.city

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CityDao {

//    @Query("SELECT * FROM business WHERE business_user_id= :userId ")
//    fun getBusinessLiveData(userId: Long?): LiveData<MutableList<Business>>
//
//    @Query("SELECT * FROM business WHERE business_user_id= :userId ")
//    fun getBusinessData(userId: Long?): MutableList<Business>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city: City)

//    @Update
//    fun updateBusiness(vararg business: Business)

    @Delete
    fun delete(city: City)

    /*@Query("UPDATE business SET name = :businessName WHERE business_user_id = :userId")
    fun updateBusinessName(userId: Long?, businessName: String?)

    @Query("UPDATE business SET name = :businessName WHERE business_user_id = :userId")
    fun updateBusinessDescription(userId: Long?, businessName: String?)*/

}
