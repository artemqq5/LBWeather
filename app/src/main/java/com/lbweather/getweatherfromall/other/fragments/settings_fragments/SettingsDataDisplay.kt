package com.lbweather.getweatherfromall.other.fragments.settings_fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.lbweather.getweatherfromall.R

class SettingsDataDisplay : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_data_display, rootKey)
    }
}