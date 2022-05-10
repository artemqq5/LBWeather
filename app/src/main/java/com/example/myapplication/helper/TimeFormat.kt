package com.example.myapplication.helper

import java.text.SimpleDateFormat
import java.util.*

object TimeFormat {

    const val YEAR_MONTH_DAY_HOUR_MINUTE = "yyyy-MM-dd HH:mm"

    const val YEAR_MONTH_DAY = "yyyy-MM-dd"
    const val HOUR_MINUTE = "HH:mm"
    const val HOUR_MINUTE_AA = "hh:mm aa"

    const val DAYWEEK_DAY_MONTH_YEAR = "EEEE, dd MMMM yyyy"


    fun String.getParsingTime(patternFrom: String, patternTo: String): String {

        val returnDate: String

        val writeFormat = SimpleDateFormat(patternFrom, Locale.US)
        val readFormat = SimpleDateFormat(patternTo, Locale.getDefault())

        // return data with no parse if format has exception
        returnDate = try {
            readFormat.format(writeFormat.parse(this)!!)
        } catch (e: Exception) {
            this
        }

        return returnDate
    }


}
