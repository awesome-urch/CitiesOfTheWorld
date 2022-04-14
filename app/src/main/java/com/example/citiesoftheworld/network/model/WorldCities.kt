package com.example.citiesoftheworld.network.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable


/*

@Parcelize
@JsonClass(generateAdapter = true)
data class WorldCities (
    var data: Data?,
    var time: Int?
) :Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Data (
//    var items: ArrayList<Items>?,
    var pagination: Pagination?
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Pagination (
    var current_page: Int?,
    var last_page: Int?,
    var per_page: Int?,
    var total: Int?
) : Parcelable

//@Parcelize
//@JsonClass(generateAdapter = true)
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

@Parcelize
@JsonClass(generateAdapter = true)
data class Items (
    var id: Int?,
    var name: String?,
    var local_name: String?,
    var lat: Double?,
    var lng: Double?,
    var created_at: String?,
    var updated_at: String?,
    var country_id: Int?,
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Country (
    var id: Int?,
    var name: String?,
    var code: String?,
    var created_at: String?,
    var updated_at: String?,
    var continent_id: Int?
) : Parcelable

*/

@Keep
@Serializable
//@JsonClass(generateAdapter = true)
data class WorldCities (
    var data: Data?,
    var time: Int?
)

@Serializable
//@JsonClass(generateAdapter = true)
data class Data (
    var items: List<Items>?,
    var pagination: Pagination?
)

@Serializable
//@JsonClass(generateAdapter = true)
data class Pagination (
    var current_page: Int?,
    var last_page: Int?,
    var per_page: Int?,
    var total: Int?
)

@Serializable
data class Items (
    var id: String?,
    var name: String?,
    var local_name: String?,
    var lat: String?,
    var lng: String?,
    var created_at: String?,
    var updated_at: String?,
    var country_id: String?,
    var country: Country?,
)

@Serializable
data class Country (
    var id: String?,
    var name: String?,
    var code: String?,
    var created_at: String?,
    var updated_at: String?,
    var continent_id: String?
)

