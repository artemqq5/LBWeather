package com.lbweather.getweatherfromall.data.optional.datastore

//import android.content.Context
//import android.content.SharedPreferences
//import androidx.preference.PreferenceManager
//import com.lbweather.getweatherfromall.R
//import com.lbweather.getweatherfromall.helper.FromStr.fromArray
//
//class MyStorageSettings(private val context: Context) {
//
//    private val sharedPreferences: SharedPreferences by lazy {
//        PreferenceManager.getDefaultSharedPreferences(context)
//    }
//
//    private val tempUnitKEY = "unit_temperature"
//    private val timeFormatKEY = "time_format"
//
//    fun getTimeFormat(): String {
//        return sharedPreferences.getString(
//            timeFormatKEY,
//            context.fromArray(R.array.list_times_value, 0)
//        ) ?: context.fromArray(R.array.list_times_value, 0)
//    }
//
//    fun getTempUnit(): String {
//        return sharedPreferences.getString(
//            tempUnitKEY,
//            context.fromArray(R.array.list_temperature_value, 0)
//        ) ?: context.fromArray(R.array.list_temperature_value, 0)
//    }
//
//}