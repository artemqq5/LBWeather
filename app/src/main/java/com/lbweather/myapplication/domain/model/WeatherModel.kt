package com.lbweather.myapplication.domain.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherModel(
    val current: Current,
    val forecast: Forecast,
    val location: Location
)