package com.lbweather.getweatherfromall.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.lbweather.getweatherfromall.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoragePreferenceImp(private val context: Context) : StoragePreference {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "my-storage-location")
    private val timeFormatKey = stringPreferencesKey("time-format")
    private val unitTemperatureKey = stringPreferencesKey("unit-temperature")

    override val getTimeFormat: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[timeFormatKey] ?: context.resources.getStringArray(R.array.list_times_value)[0]
    }

    override val getUnitTemperature: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[unitTemperatureKey]
            ?: context.resources.getStringArray(R.array.list_temperature_value)[0]
    }

    override suspend fun setTimeFormat(timeFormat: String) {
        context.dataStore.edit { settings ->
            settings[timeFormatKey] = timeFormat
        }
    }

    override suspend fun setUnitTemperature(unitTemperature: String) {
        context.dataStore.edit { settings ->
            settings[unitTemperatureKey] = unitTemperature
        }
    }

}