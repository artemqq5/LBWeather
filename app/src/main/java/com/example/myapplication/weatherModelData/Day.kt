package com.example.myapplication.weatherModelData

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Day(
    val avghumidity: Double,
    val avgtemp_c: Double,
    val condition: ConditionX,
    val daily_chance_of_rain: Int,
    val daily_chance_of_snow: Int,
    val maxtemp_c: Double,
    val maxwind_kph: Double,
    val mintemp_c: Double,
    val totalprecip_mm: Double,
    val uv: Double
)