package com.lbweather.myapplication.domain.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ConditionX(
    val icon: String,
    val text: String
)