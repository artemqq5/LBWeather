package com.lbweather.getweatherfromall.domain.usecase

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DateTimeUseCase {

    fun timeEpochToDate(timeEpoch: Long): Date {
        return Date(timeEpoch * 1000)
    }

    fun getFormatTimeForLocale(time: Date): String {
        val defaultLocale = Locale.getDefault()
        val formatter24 = SimpleDateFormat("HH:mm", defaultLocale)
        val formatter12 = SimpleDateFormat("h:mm a", defaultLocale)

        val time24 = formatter24.format(time)
        val time12 = formatter12.format(time)

        return if (time12.contains("AM") || time12.contains("PM")) time12 else time24
    }

    fun getFormatTime12(time: Date): String {
        val formatter12 = SimpleDateFormat("h:mm a", Locale.US)

        return formatter12.format(time)
    }

    fun getFormatTime24(time: Date): String {
        val defaultLocale = Locale.getDefault()
        val formatter24 = SimpleDateFormat("HH:mm", defaultLocale)

        return formatter24.format(time)
    }

    fun isDateAfterCurrent(date: Date): Boolean {
        return date.after(getCurrentDate())
    }

    private fun getCurrentDate(): Date {
        return Calendar.getInstance().time
    }
}