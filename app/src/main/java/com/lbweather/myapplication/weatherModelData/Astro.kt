package com.lbweather.myapplication.weatherModelData

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Astro(
    val sunrise: String,
    val sunset: String
)