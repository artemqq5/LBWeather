package com.lbweather.myapplication.weatherModelData

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Forecast(
    val forecastday: List<Forecastday>
)