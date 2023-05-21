package com.lbweather.myapplication.other.fragments.settings_fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.lbweather.myapplication.R

class SettingsDataDisplay : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_data_display, rootKey)
    }
}