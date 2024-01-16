package com.lbweather.getweatherfromall.domain.optional

//import com.lbweather.getweatherfromall.data.datastore.MyStorageSettings
//import com.lbweather.getweatherfromall.helper.TimeFormat.HOUR
//import com.lbweather.getweatherfromall.helper.TimeFormat.HOUR_AA
//
//class UseCaseSettings(private val myStorageSettings: MyStorageSettings) {
//
//    fun getTimeFormat(): String {
//        return parseTime()
//    }
//
//    fun getTempUnit(): TempUnit {
//        return parseTemp()
//    }
//
//    private fun parseTime(): String {
//        return when(myStorageSettings.getTimeFormat()) {
//            "america" -> HOUR_AA
//            else -> HOUR
//        }
//    }
//
//    private fun parseTemp(): TempUnit {
//        return when(myStorageSettings.getTempUnit()) {
//            "fahrenheit" -> TempUnit.FAHRENHEIT
//            else -> TempUnit.CELSIUS
//        }
//    }
//
//    enum class TempUnit {
//        FAHRENHEIT, CELSIUS
//    }
//
//}
