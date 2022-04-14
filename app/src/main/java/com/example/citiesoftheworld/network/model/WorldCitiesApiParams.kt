package com.example.citiesoftheworld.network.model

data class WorldCitiesApiParams(
    var nameFilter: String,
    var page: Int,
    var include: String = "country"
)