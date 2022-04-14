package com.example.citiesoftheworld.network.model

sealed class WorldCitiesResultState {
    data class Success(val itemList: List<Items>) : WorldCitiesResultState()
    data class Error(val message: String) : WorldCitiesResultState()
    object Loading : WorldCitiesResultState()
    object Empty : WorldCitiesResultState()
}