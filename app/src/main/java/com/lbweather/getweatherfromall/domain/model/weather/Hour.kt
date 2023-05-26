package com.lbweather.getweatherfromall.domain.model.weather

import android.os.Parcelable
import com.lbweather.getweatherfromall.other.helper.TimeFormat
import com.lbweather.getweatherfromall.other.helper.TimeFormat.getParsingTimeHour
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
    val wind_mph: Double,
) : Parcelable {
    val timeAA: String
        get() = time.getParsingTimeHour(
            TimeFormat.YEAR_MONTH_DAY_HOUR_MINUTE,
            TimeFormat.HOUR_AA
        )

    val tempCParsed: String
        get() = "${temp_c.toInt()}°"
}