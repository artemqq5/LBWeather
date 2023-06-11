package com.lbweather.getweatherfromall.domain.repository

import androidx.fragment.app.FragmentActivity
import com.lbweather.getweatherfromall.domain.model.weather.WeatherModel
import com.lbweather.getweatherfromall.data.database.LocationTable
import com.lbweather.getweatherfromall.domain.network.ConnectionManager
import com.lbweather.getweatherfromall.presentation.locationsFragment.SetLocationByGPS
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface DefaultRepository {

    suspend fun getWeather(location: String): Response<WeatherModel>

    suspend fun getLocationList(): List<LocationTable>
    suspend fun setNewLocation(locationTable: LocationTable)
    suspend fun deleteLocation(locationTable: LocationTable)

    fun getCurrentLocation(): Flow<LocationTable?>
    suspend fun getLastCurrentLocation(): LocationTable
    suspend fun setCurrentLocation(locationTable: LocationTable)

    fun getConnectionFlow(): Flow<ConnectionManager.StatusInternet>
    fun isInternetActive(): Boolean

    fun FragmentActivity.initPermissionRequestLauncher()
    fun checkLocationPermission()
    fun initCallBackSetLocationByGPS(interfaceModel: SetLocationByGPS)

    fun getTimeFormat(): String
    fun getTempUnit(): String
}