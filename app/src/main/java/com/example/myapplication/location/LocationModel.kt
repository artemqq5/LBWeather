package com.example.myapplication.location

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationModel(
    val lat: String,
    val lon: String,
    val locality: String
)