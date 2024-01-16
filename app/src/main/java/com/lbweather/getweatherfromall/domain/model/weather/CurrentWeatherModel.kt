package com.lbweather.getweatherfromall.domain.model.weather


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrentWeatherModel(
    @Json(name = "condition")
    val conditionModel: ConditionModel?,
    @Json(name = "last_updated")
    val lastUpdated: String?,
    @Json(name = "last_updated_epoch")
    val lastUpdatedEpoch: Int?,
    @Json(name = "temp_c")
    val tempC: Double?,
    @Json(name = "temp_f")
    val tempF: Double?
)