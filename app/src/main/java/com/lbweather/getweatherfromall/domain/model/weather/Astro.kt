package com.lbweather.getweatherfromall.domain.model.weather

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Astro(
    val sunrise: String,
    val sunset: String
)