package com.lbweather.getweatherfromall.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lbweather.getweatherfromall.MyApp
import com.lbweather.getweatherfromall.domain.model.LocationUserModel
import com.lbweather.getweatherfromall.domain.repository.LocationRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocationViewModel(private val locationRepository: LocationRepository) : ViewModel() {

    private val excHandler = CoroutineExceptionHandler { _, throwable ->
        MyApp.logData("Coroutine Exception. LocationViewModel ($throwable)")
    }

    val allLocationData = locationRepository.getAllLocation()

    private val _searchLocationFlow = MutableSharedFlow<LocationUserModel>()
    val searchLocationFlow = _searchLocationFlow.asSharedFlow()

    fun searchLocation(location: String, lang: String) {
        viewModelScope.launch(Dispatchers.IO + excHandler) {
            locationRepository.searchLocation(location, lang)?.let {
                _searchLocationFlow.emit(it)
            }
        }
    }

    suspend fun getActiveLocation(): LocationUserModel {
        return locationRepository.getActiveLocation()
    }

    fun updateActiveLocation(locationUserModel: LocationUserModel) {
        viewModelScope.launch(Dispatchers.IO+excHandler) {
            locationRepository.setActiveLocation(locationUserModel)
        }
    }

    fun insertNewLocationIfNotExists(locationUserModel: LocationUserModel) {
        viewModelScope.launch(Dispatchers.IO+excHandler) {
            locationRepository.addNewLocation(locationUserModel)
        }
    }

    fun deleteLocation(locationUserModel: LocationUserModel) {
        viewModelScope.launch(Dispatchers.IO+excHandler) {
            locationRepository.deleteLocation(locationUserModel)
        }
    }

}