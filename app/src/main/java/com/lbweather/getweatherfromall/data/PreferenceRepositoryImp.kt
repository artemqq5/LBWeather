package com.lbweather.getweatherfromall.data

import com.lbweather.getweatherfromall.data.storage.StoragePreference
import com.lbweather.getweatherfromall.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow

class PreferenceRepositoryImp(private val storagePreference: StoragePreference) :
    PreferenceRepository {

    override val getTimeFormat: Flow<String>
        get() = storagePreference.getTimeFormat

    override suspend fun setTimeFormat(timeFormat: String) {
        storagePreference.setTimeFormat(timeFormat)
    }

    override val getUnitTemperature: Flow<String>
        get() = storagePreference.getUnitTemperature

    override suspend fun setUnitTemperature(unitTemperature: String) {
        storagePreference.setUnitTemperature(unitTemperature)
    }

}