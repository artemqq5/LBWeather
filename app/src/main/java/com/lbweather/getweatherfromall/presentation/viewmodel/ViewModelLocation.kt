package com.lbweather.getweatherfromall.presentation.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lbweather.getweatherfromall.MyApp.Companion.logData
import com.lbweather.getweatherfromall.data.database.LocationTable
import com.lbweather.getweatherfromall.domain.model.weather.Location
import com.lbweather.getweatherfromall.domain.repository.DefaultRepository
import com.lbweather.getweatherfromall.presentation.locationsFragment.SetLocationByGPS
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ViewModelLocation(
    private val repository: DefaultRepository,
) : ViewModel(), SetLocationByGPS {

    private val excHandler = CoroutineExceptionHandler { _, throwable ->
        logData("Coroutine Exception. ViewModelLocation ($throwable)")
    }

    private val mutableFlowLocationList = MutableSharedFlow<List<LocationTable>>(replay = 1)
    val flowLocationList: SharedFlow<List<LocationTable>>
        get() = mutableFlowLocationList.asSharedFlow()

    private val mutableLocationFromGPS = MutableSharedFlow<Location>()
    val locationFromGPS: SharedFlow<Location>
        get() = mutableLocationFromGPS.asSharedFlow()

    private fun getLocationDataList() {
        viewModelScope.launch(Dispatchers.IO + excHandler) {
            mutableFlowLocationList.emit(repository.getLocationList())
        }
    }

    fun setLocationData(locationTable: LocationTable) {
        viewModelScope.launch(Dispatchers.IO + excHandler) {
            repository.setNewLocation(locationTable)
            getLocationDataList()
        }
    }

    fun deleteLocationData(locationTable: LocationTable) {
        viewModelScope.launch(Dispatchers.IO + excHandler) {
            repository.deleteLocation(locationTable)
            getLocationDataList()
        }
    }

    init {
        getLocationDataList()
        repository.initCallBackSetLocationByGPS(this)
    }

    val flowCurrentLocation: Flow<LocationTable?> =
        repository.getCurrentLocation()

    suspend fun getLastCurrentLocation(): LocationTable {
        return repository.getLastCurrentLocation()
    }

    fun setCurrentLocationData(locationTable: LocationTable) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.setCurrentLocation(locationTable)
        }
    }


    // LOCATION GPS
    fun FragmentActivity.initLocationRequestPermission() {
        repository.apply {
            initPermissionRequestLauncher()
        }
    }

    fun checkLocationPermission() {
        repository.checkLocationPermission()
    }

    override fun setGSPLocation(locationName: String?) {
        locationName?.let {
            logData("setGSPLocation = $it")
            viewModelScope.launch(Dispatchers.IO + excHandler) {
                repository.getWeather(it).body()?.let { weatherModel ->
                    mutableLocationFromGPS.emit(weatherModel.location)
                }
            }
        }
    }
}