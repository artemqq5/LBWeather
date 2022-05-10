package com.example.myapplication.weatherModelData

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Astro(
    val sunrise: String,
    val sunset: String
)