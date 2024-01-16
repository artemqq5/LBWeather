package com.lbweather.getweatherfromall.domain.model

data class LocationUserModel(
    val lat: Double?,
    val lon: Double?,
    val country: String?,
    val localTimeEpoch: Int?,
    val localTime: String?,
    val shortLocation: String?,
)
