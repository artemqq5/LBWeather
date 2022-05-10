package com.example.myapplication.helper.locationModel

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationModel (
    val lat: String,
    val lon: String,
    val locality:String
)