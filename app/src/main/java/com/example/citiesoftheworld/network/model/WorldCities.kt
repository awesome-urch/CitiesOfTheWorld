package com.example.citiesoftheworld.network.model

//import com.squareup.moshi.Json
import kotlinx.serialization.Serializable


@Serializable
data class WorldCities (
    var data: Data?,
    var time: Int?
)

@Serializable
data class Data (
//    var items: ArrayList<Items>?,
    var pagination: Pagination?
)

@Serializable
data class Pagination (
    var current_page: Int?,
    var last_page: Int?,
    var per_page: Int?,
    var total: Int?
)

//@Serializable
//data class Items (
//    var id: Int?,
//    var name: String?,
//    var local_name: String?,
//    var lat: Double?,
//    var lng: Double?,
//    var created_at: String?,
//    var updated_at: String?,
//    var country_id: Int?,
//    @Json(name = "country")
//    var __country: Country?
//) {
//    val country
//        get() = __country!!
//}

//@Serializable
//data class Items (
//    var id: Int?,
//    var name: String?,
//    var local_name: String?,
//    var lat: Double?,
//    var lng: Double?,
//    var created_at: String?,
//    var updated_at: String?,
//    var country_id: Int?,
//)

@Serializable
data class Country (
    var id: Int?,
    var name: String?,
    var code: String?,
    var created_at: String?,
    var updated_at: String?,
    var continent_id: Int?
)

