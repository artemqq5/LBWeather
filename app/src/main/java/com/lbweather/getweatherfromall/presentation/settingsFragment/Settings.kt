package com.lbweather.getweatherfromall.presentation.settingsFragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.lbweather.getweatherfromall.R

class Settings : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}