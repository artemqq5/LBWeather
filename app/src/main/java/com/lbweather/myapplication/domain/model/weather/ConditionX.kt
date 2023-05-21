package com.lbweather.myapplication.domain.model.weather

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ConditionX(
    val icon: String,
    val text: String
)