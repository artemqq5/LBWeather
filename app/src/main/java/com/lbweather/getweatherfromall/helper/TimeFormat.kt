package com.lbweather.getweatherfromall.helper

import com.lbweather.getweatherfromall.MyApp.Companion.logData
import java.text.SimpleDateFormat
import java.util.*

object TimeFormat {

    const val YEAR_MONTH_DAY_HOUR_MINUTE = "yyyy-MM-dd HH:mm"
    const val LOCAL_DATE_PATTERN = "EEE MMM dd HH:mm:ss zzz yyyy"
    const val HOUR_MINUTE = "HH:mm"

    const val HOUR_AA = "hh aa"
    const val HOUR = "HH"


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
        val dateNow = Calendar.getInstance().time.toString()

        val changeFormatServer = time.getParsingTime(YEAR_MONTH_DAY_HOUR_MINUTE, HOUR_MINUTE)
        val changeFormatLocal = dateNow.getParsingTime(LOCAL_DATE_PATTERN, HOUR_MINUTE)

        logData("$changeFormatServer serv | $changeFormatLocal local")

        val formatter = SimpleDateFormat(HOUR_MINUTE, Locale.US)
        val dServer = formatter.parse(changeFormatServer)
        val dLocal = formatter.parse(changeFormatLocal)


        return dServer?.after(dLocal) ?: false
    }


}
