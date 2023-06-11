package com.lbweather.getweatherfromall.data.datastore

import android.content.Context
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class MyStorageLocation(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "my-storage-location")
    private val CURRENT_LOCATION_KEY = stringPreferencesKey("current-location")

    private val defaultLocation = "{\"country\": \"Ukraine\", \"lat\": 50.43, \"localtime\": " +
            "\"2023-05-21 13:28\", \"localtime_epoch\": 1684664900, \"lon\": 30.52, \"name\": " +
            "\"Kiev\", \"region\": \"Kyyivs'ka Oblast'\", \"tz_id\": \"Europe/Kiev\"," +
            " \"statusActive\": \"${View.INVISIBLE}\"}\n"

    val myCurrentLocation: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[CURRENT_LOCATION_KEY] ?: defaultLocation
    }

    suspend fun myLastCurrentLocation(): String {
        return context.dataStore.data.first()[CURRENT_LOCATION_KEY] ?: run {
            defaultLocation.apply {
                context.dataStore.edit { settings ->
                    settings[CURRENT_LOCATION_KEY] = this
                }
            }

        }
    }

    suspend fun setCurrentLocation(newLocationList: String) {
        context.dataStore.edit { settings ->
            settings[CURRENT_LOCATION_KEY] = newLocationList
        }
    }

}