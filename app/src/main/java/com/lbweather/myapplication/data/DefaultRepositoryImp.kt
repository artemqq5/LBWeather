package com.lbweather.myapplication.data

import com.lbweather.myapplication.domain.UseCaseCurrentLocation
import com.lbweather.myapplication.domain.UseCaseDataLocation
import com.lbweather.myapplication.domain.UseCaseGetData
import com.lbweather.myapplication.domain.model.weather.WeatherModel
import com.lbweather.myapplication.domain.repository.DefaultRepository
import com.lbweather.myapplication.data.database.LocationTable
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class DefaultRepositoryImp(
    private val useCaseGetData: UseCaseGetData,
    private val useCaseDataLocation: UseCaseDataLocation,
    private val useCaseCurrentLocation: UseCaseCurrentLocation
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


}