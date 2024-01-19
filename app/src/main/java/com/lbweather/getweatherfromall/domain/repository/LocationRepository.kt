package com.lbweather.getweatherfromall.domain.repository

import com.lbweather.getweatherfromall.domain.model.LocationUserModel
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    suspend fun searchLocation(location: String, lang: String): LocationUserModel?

    fun getAllLocation(): Flow<List<LocationUserModel>>
    suspend fun addNewLocation(locationUserModel: LocationUserModel)
    suspend fun deleteLocation(locationUserModel: LocationUserModel)

    suspend fun getActiveLocation(): LocationUserModel
    suspend fun setActiveLocation(locationUserModel: LocationUserModel)

}