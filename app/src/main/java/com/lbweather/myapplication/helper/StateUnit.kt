package com.lbweather.myapplication.helper

import com.lbweather.myapplication.R
import com.lbweather.myapplication.fragments.settings_fragments.PreferencesObject.TIME_FORMAT_APP
import com.lbweather.myapplication.fragments.settings_fragments.PreferencesObject.UNIT_OF_SPEED
import com.lbweather.myapplication.fragments.settings_fragments.PreferencesObject.UNIT_OF_TEMPERATURE
import com.lbweather.myapplication.fragments.settings_fragments.PreferencesObject.getValuePreferenceSpeed
import com.lbweather.myapplication.fragments.settings_fragments.PreferencesObject.getValuePreferenceTemperature
import com.lbweather.myapplication.fragments.settings_fragments.PreferencesObject.getValuePreferenceTimeFormat
import com.lbweather.myapplication.helper.FromStr.fromArray
import com.lbweather.myapplication.helper.FromStr.fromStr

object StateUnit {

    fun isCelsius(): Boolean {
        return getValuePreferenceTemperature(UNIT_OF_TEMPERATURE) == fromStr(
            R.string.celsius
        )
    }

    fun isTime24(): Boolean {
        return getValuePreferenceTimeFormat(TIME_FORMAT_APP) == fromArray(
            R.array.list_times_value,
            0
        )
    }

    fun isKilometer(): Boolean {
        return getValuePreferenceSpeed(UNIT_OF_SPEED) == fromArray(
            R.array.list_speed_value,
            0
        )
    }

}