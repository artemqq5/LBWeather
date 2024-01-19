package com.lbweather.getweatherfromall.domain.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationUserModel(
    val lat: Double,
    val lon: Double,
    val region: String,
    val status: Boolean,
    val country: String,
    val shortLocation: String,
)
