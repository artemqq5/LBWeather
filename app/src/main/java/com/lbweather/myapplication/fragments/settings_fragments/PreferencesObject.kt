package com.lbweather.myapplication.fragments.settings_fragments

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.lbweather.myapplication.R
import com.lbweather.myapplication.MainActivity.Companion.main_context
import com.lbweather.myapplication.helper.FromStr.fromArray
import com.lbweather.myapplication.helper.FromStr.fromStr

object PreferencesObject {

    const val CHANCE_RAIN_DAY = "chance_rain_day"
    const val CHANCE_SNOW_DAY = "chance_snow_day"
    const val ULTRAVIOLET_DAY = "uv_index_day"

    const val CHANCE_RAIN_HOUR = "chance_rain_hour"
    const val CHANCE_SNOW_HOUR = "chance_snow_hour"
    const val ULTRAVIOLET_HOUR = "uv_index_hour"
    const val FEEL_TEMP_HOUR = "feel_temp_hour"

    const val UNIT_OF_TEMPERATURE = "unit_temperature"
    const val UNIT_OF_SPEED = "unit_speed"
    const val TIME_FORMAT_APP = "time_format"

    // get shared shared preferences
    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(main_context)
    }

    // get value from shared preferences
    fun getValuePreference(prefKey: String) = sharedPreferences.getBoolean(prefKey, false)

    fun getValuePreferenceTimeFormat(prefKey: String): String = sharedPreferences.getString(
        prefKey, fromArray(R.array.list_times_value, 0)
    ) ?: fromArray(R.array.list_times_value, 0)

    fun getValuePreferenceSpeed(prefKey: String): String = sharedPreferences.getString(
        prefKey, fromArray(R.array.list_speed_value, 0)
    ) ?: fromArray(R.array.list_speed_value, 0)

    fun getValuePreferenceTemperature(prefKey: String): String {
        val value = sharedPreferences.getString(
            prefKey, fromArray(R.array.list_temperature_value, 0)
        ) ?: fromArray(R.array.list_temperature_value, 0)

        return parseUnitOfTemperature(value)
    }

    private fun parseUnitOfTemperature(str: String): String {
        return when (str) {
            fromArray(R.array.list_temperature_value, 0) -> {
                fromStr(
                    R.string.celsius
                )
            }

            fromArray(R.array.list_temperature_value, 1) -> {
                fromStr(
                    R.string.fahrenheit
                )
            }
            else -> fromStr(
                R.string.celsius
            )
        }
    }

}