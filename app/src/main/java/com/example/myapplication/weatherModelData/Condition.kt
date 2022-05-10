package com.example.myapplication.weatherModelData

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Condition(
    val icon: String,
    val text: String
)