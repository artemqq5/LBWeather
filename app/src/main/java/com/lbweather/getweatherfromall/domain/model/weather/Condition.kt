package com.lbweather.getweatherfromall.domain.model.weather

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Condition(
    val icon: String,
    val text: String
)