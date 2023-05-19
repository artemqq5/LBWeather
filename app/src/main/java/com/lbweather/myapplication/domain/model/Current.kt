package com.lbweather.myapplication.domain.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Current(
    val air_quality: AirQuality,
    val condition: Condition,
    val feelslike_c: Double,
    val feelslike_f: Double,
    val humidity: Int,
    val last_updated: String,
    val temp_c: Double,
    val temp_f: Double,
    val uv: Double,
    val wind_kph: Double,
    val wind_mph: Double
)