package com.example.myapplication.fragments.settings_fragments

import androidx.preference.PreferenceManager
import com.example.myapplication.MainActivity.Companion.main_context

object PreferencesObject {

    const val CHANCE_RAIN_DAY = "chance_rain_day"
    const val CHANCE_SNOW_DAY = "chance_snow_day"
    const val ULTRAVIOLET_DAY = "uv_index_day"

    const val CHANCE_RAIN_HOUR = "chance_rain_hour"
    const val CHANCE_SNOW_HOUR = "chance_snow_hour"
    const val ULTRAVIOLET_HOUR = "uv_index_hour"
    const val FEEL_TEMP_HOUR = "feel_temp_hour"

    // get shared shared preferences
    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(main_context)

    // get value from shared preferences
    fun getValuePreference(prefKey: String) = sharedPreferences.getBoolean(prefKey, false)

}