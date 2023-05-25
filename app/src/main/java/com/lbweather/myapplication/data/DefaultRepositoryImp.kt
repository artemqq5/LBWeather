package com.lbweather.myapplication.data

import androidx.fragment.app.FragmentActivity
import com.lbweather.myapplication.domain.model.weather.WeatherModel
import com.lbweather.myapplication.domain.repository.DefaultRepository
import com.lbweather.myapplication.data.database.LocationTable
import com.lbweather.myapplication.domain.*
import com.lbweather.myapplication.domain.network.ConnectionManager
import com.lbweather.myapplication.presentation.locationsFragment.SetLocationByGPS
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class DefaultRepositoryImp(
    private val useCaseGetData: UseCaseGetData,
    private val useCaseDataLocation: UseCaseDataLocation,
    private val useCaseCurrentLocation: UseCaseCurrentLocation,
    private val useCaseInternet: UseCaseInternet,
    private val useCaseLocationPermission: UseCaseLocationPermission
) : DefaultRepository {

    override suspend fun getWeather(location: String): Response<WeatherModel> {
        return useCaseGetData.getWeatherData(location)
    }

    override suspend fun getLocationList(): List<LocationTable> {
        return useCaseDataLocation.getDataLocationList()
    }
    override suspend fun setNewLocation(locationTable: LocationTable) {
        useCaseDataLocation.setDataLocation(locationTable)
    }
    override suspend fun deleteLocation(locationTable: LocationTable) {
        useCaseDataLocation.deleteDataLocation(locationTable)
    }


    override fun getCurrentLocation(): Flow<LocationTable?> {
        return useCaseCurrentLocation.getCurrentDataLocation()
    }
    override suspend fun getLastCurrentLocation(): LocationTable {
        return useCaseCurrentLocation.getLastCurrentLocation()
    }
    override suspend fun setCurrentLocation(locationTable: LocationTable) {
        useCaseCurrentLocation.setCurrentLocation(locationTable)
    }

    override fun getConnectionFlow(): Flow<ConnectionManager.StatusInternet> {
        return useCaseInternet.getConnectionFlow()
    }
    override fun isInternetActive(): Boolean {
        return useCaseInternet.getStatusInternet()
    }

    override fun FragmentActivity.initPermissionRequestLauncher() {
        useCaseLocationPermission.apply {
            initPermissionRequestLauncher()
        }
    }
    override fun checkLocationPermission() {
        useCaseLocationPermission.checkLocationPermission()
    }
    override fun initCallBackSetLocationByGPS(interfaceModel: SetLocationByGPS) {
        useCaseLocationPermission.initCallBackSetLocationByGPS(interfaceModel)
    }

}