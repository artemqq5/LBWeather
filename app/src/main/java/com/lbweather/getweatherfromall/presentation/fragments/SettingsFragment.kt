package com.lbweather.getweatherfromall.presentation.fragments

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.lbweather.getweatherfromall.R
import com.lbweather.getweatherfromall.presentation.viewmodel.PreferenceViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SettingsFragment : PreferenceFragmentCompat() {

    private val preferenceViewModel: PreferenceViewModel by activityViewModel()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val temperaturePreference: ListPreference? = findPreference("unit-temperature")
        val timePreference: ListPreference? = findPreference("time-format")

        temperaturePreference?.let { preference ->
            preference.setOnPreferenceChangeListener { _, newValue ->
                preferenceViewModel.setUnitTemperature(newValue as String)
                true
            }
        }

        timePreference?.let { preference ->
            preference.setOnPreferenceChangeListener { _, newValue ->
                preferenceViewModel.setTimeFormat(newValue as String)
                true
            }
        }
    }
}