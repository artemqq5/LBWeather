package com.example.myapplication.weatherModelData

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Current(
    val air_quality: AirQuality,
    val condition: Condition,
    val feelslike_c: Double,
    val humidity: Int,
    val last_updated: String,
    val temp_c: Double,
    val uv: Double,
    val wind_kph: Double
)