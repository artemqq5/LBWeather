package com.lbweather.getweatherfromall.domain.repository

import com.lbweather.getweatherfromall.data.database.LocationTable

interface LocationRepository {

    suspend fun getLocationList(): List<LocationTable>
    suspend fun setNewLocation(locationTable: LocationTable)
    suspend fun deleteLocation(locationTable: LocationTable)

    suspend fun getCurrentLocation(): LocationTable
    suspend fun getLastCurrentLocation(): LocationTable
    suspend fun setCurrentLocation(locationTable: LocationTable)

}