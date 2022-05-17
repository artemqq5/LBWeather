package com.lbweather.myapplication.weatherModelData

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AirQuality(
    val co: Double,
    @Json(name = "gb-defra-index")
    val gb_defra_index: Int,
    val no2: Double,
    val o3: Double,
    val pm10: Double,
    val pm2_5: Double,
    val so2: Double,
    @Json(name = "us-epa-index")
    val us_epa_index: Int
)