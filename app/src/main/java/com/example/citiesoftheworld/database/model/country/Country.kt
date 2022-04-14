package com.example.citiesoftheworld.database.model.country

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "country")
data class Country(
    @PrimaryKey
    @ColumnInfo(name = "id") var id: Long,
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "code") var code:String?,
    @ColumnInfo(name = "continent_id") var continentId:String?
)
