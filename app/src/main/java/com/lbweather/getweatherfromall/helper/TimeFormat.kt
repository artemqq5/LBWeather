package com.lbweather.getweatherfromall.helper

import java.text.SimpleDateFormat
import java.util.*

object TimeFormat {

    const val YEAR_MONTH_DAY_HOUR_MINUTE = "yyyy-MM-dd HH:mm"

    const val HOUR_AA = "hh aa"
    const val HOUR = "HH"

    const val DAYWEEK_DAY_MONTH_YEAR = "EEEE, dd MMMM yyyy"


    fun String.getParsingTime(patternFrom: String, patternTo: String): String {

        val returnDate: String

        val writeFormat = SimpleDateFormat(patternFrom, Locale.US)
        val readFormat = SimpleDateFormat(patternTo, Locale.US)

        // return data with no parse if format has exception
        returnDate = try {
            readFormat.format(writeFormat.parse(this)!!)
        } catch (e: Exception) {
            this
        }

        return returnDate
    }

    // return true if date bigger then now local date
    fun compareDate(time: String): Boolean {
        val dateNow = Calendar.getInstance().time
        val formatter = SimpleDateFormat(YEAR_MONTH_DAY_HOUR_MINUTE, Locale.getDefault())

        return formatter.parse(time)?.after(dateNow) ?: false
    }


}
