package com.lbweather.myapplication.weatherModelData

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Hour(
    val chance_of_rain: Int?,
    val chance_of_snow: Int?,
    val condition: ConditionXX,
    val feelslike_c: Double,
    val feelslike_f: Double,
    val humidity: Int,
    val temp_c: Double,
    val temp_f: Double,
    var time: String,
    val uv: Double,
    val wind_kph: Double,
    val wind_mph: Double
): Parcelable