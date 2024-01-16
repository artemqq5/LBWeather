package com.lbweather.getweatherfromall.domain.model.weather


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DayModel(
    @Json(name = "condition")
    val conditionModel: ConditionModel?,
    @Json(name = "maxtemp_c")
    val maxtempC: Double?,
    @Json(name = "maxtemp_f")
    val maxtempF: Double?,
    @Json(name = "mintemp_c")
    val mintempC: Double?,
    @Json(name = "mintemp_f")
    val mintempF: Double?
) {
    val maxTempCParsed: String
        get() = "H:${maxtempC?.toInt()}°"

    val minTempCParsed: String
        get() = "L:${mintempC?.toInt()}°"

    val maxTempFParsed: String
        get() = "H:${maxtempF?.toInt()}°"

    val minTempFParsed: String
        get() = "L:${mintempF?.toInt()}°"
}