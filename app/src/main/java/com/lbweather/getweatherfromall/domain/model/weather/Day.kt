package com.lbweather.getweatherfromall.domain.model.weather

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Day(
    val avghumidity: Double,
    val avgtemp_c: Double,
    val avgtemp_f: Double,
    val condition: ConditionX,
    val daily_chance_of_rain: Int,
    val daily_chance_of_snow: Int,
    val maxtemp_c: Double,
    val maxtemp_f: Double,
    val maxwind_kph: Double,
    val maxwind_mph: Double,
    val mintemp_c: Double,
    val mintemp_f: Double,
    val totalprecip_mm: Double,
    val uv: Double,
) {
    val maxTempCParsed: String
        get() = "H:${maxtemp_c.toInt()}°"

    val minTempCParsed: String
        get() = "L:${mintemp_c.toInt()}°"
}