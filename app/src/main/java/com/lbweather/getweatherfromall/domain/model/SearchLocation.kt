package com.lbweather.getweatherfromall.domain.model


import com.lbweather.getweatherfromall.domain.model.weather.LocationDataModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchLocation(
    @Json(name = "location")
    val location: LocationDataModel
)