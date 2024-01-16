package com.lbweather.getweatherfromall.domain.model.weather


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ConditionModel(
    @Json(name = "icon")
    val icon: String?,
    @Json(name = "text")
    val text: String?
)