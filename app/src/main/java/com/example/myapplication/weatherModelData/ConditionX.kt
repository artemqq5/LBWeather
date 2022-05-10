package com.example.myapplication.weatherModelData

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ConditionX(
    val icon: String,
    val text: String
)