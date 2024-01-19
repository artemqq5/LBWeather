package com.lbweather.getweatherfromall.domain.model.weather


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherDataModel(
    @Json(name = "current")
    val currentWeatherModel: CurrentWeatherModel,
    @Json(name = "forecast")
    val forecastWeatherModel: ForecastWeatherModel,
    @Json(name = "location")
    val locationDataModel: LocationDataModel
)