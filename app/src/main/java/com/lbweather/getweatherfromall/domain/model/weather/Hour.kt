package com.lbweather.getweatherfromall.domain.model.weather

import android.os.Parcelable
import com.lbweather.getweatherfromall.helper.TimeFormat
import com.lbweather.getweatherfromall.helper.TimeFormat.getParsingTime
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

    val timeAA: (String) -> String
        get() = {
            time.getParsingTime(
                TimeFormat.YEAR_MONTH_DAY_HOUR_MINUTE,
                it
            )
        }

    val tempCParsed: String
        get() = "${temp_c.toInt()}°"

    val tempFParsed: String
        get() = "${temp_f.toInt()}°"
}