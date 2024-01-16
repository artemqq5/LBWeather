package com.lbweather.getweatherfromall.domain.model.weather


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ForecastWeatherModel(
    @Json(name = "forecastday")
    val forecastDayModel: List<ForecastDayModel>
)