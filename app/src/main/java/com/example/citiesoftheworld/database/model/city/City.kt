package com.example.citiesoftheworld.database.model.city

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city")
data class City(
    @PrimaryKey
    @ColumnInfo(name = "id") var id: Long,
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "local_name") var localName:String?,
    @ColumnInfo(name = "lat") var lat:String?,
    @ColumnInfo(name = "lng") var lng: String?,
    @ColumnInfo(name = "country_id") var countryId: Long?,
)