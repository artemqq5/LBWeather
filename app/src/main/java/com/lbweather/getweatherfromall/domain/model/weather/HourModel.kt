package com.lbweather.getweatherfromall.domain.model.weather


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HourModel(
    @Json(name = "condition")
    val conditionModel: ConditionModel,
    @Json(name = "temp_c")
    val tempC: Double,
    @Json(name = "temp_f")
    val tempF: Double,
    @Json(name = "time_epoch")
    val time: Long
)