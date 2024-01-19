package com.lbweather.getweatherfromall.data.storage

import kotlinx.coroutines.flow.Flow

interface StoragePreference {

    val getTimeFormat: Flow<String>
    suspend fun setTimeFormat(timeFormat: String)

    val getUnitTemperature: Flow<String>
    suspend fun setUnitTemperature(unitTemperature: String)
}