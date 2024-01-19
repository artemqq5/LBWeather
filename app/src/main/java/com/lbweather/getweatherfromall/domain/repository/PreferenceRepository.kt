package com.lbweather.getweatherfromall.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {
    val getTimeFormat: Flow<String>
    suspend fun setTimeFormat(timeFormat: String)

    val getUnitTemperature: Flow<String>
    suspend fun setUnitTemperature(unitTemperature: String)
}