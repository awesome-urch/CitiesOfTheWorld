package com.example.citiesoftheworld.database.model.country

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CountryDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(country: Country)



    @Delete
    fun delete(country: Country)


}
