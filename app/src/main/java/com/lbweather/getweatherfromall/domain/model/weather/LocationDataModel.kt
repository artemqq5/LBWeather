package com.lbweather.getweatherfromall.domain.model.weather


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationDataModel(
    @Json(name = "country")
    val country: String?,
    @Json(name = "lat")
    val lat: Double?,
    @Json(name = "localtime")
    val localTime: String?,
    @Json(name = "localtime_epoch")
    val localTimeEpoch: Int?,
    @Json(name = "lon")
    val lon: Double?,
    @Json(name = "name")
    val shortLocation: String?,
    @Json(name = "region")
    val region: String?,
    @Json(name = "tz_id")
    val tzId: String?
)