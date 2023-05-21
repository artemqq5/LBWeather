package com.lbweather.myapplication.domain.model.weather

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Forecastday(
    val astro: Astro,
    val date: String,
    val day: Day,
    val hour: List<Hour>
)