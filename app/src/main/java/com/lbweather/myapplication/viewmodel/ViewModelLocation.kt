package com.lbweather.myapplication.viewmodel

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lbweather.myapplication.location.LocationModel
import com.lbweather.myapplication.location.LocationRequest
import com.lbweather.myapplication.sharedPreferences.WeatherPref.getListLocationsPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelLocation @Inject constructor(
    private val locationRequest: LocationRequest
) : ViewModel() {

    val currentLocation = MutableLiveData<LocationModel?>()
    val listOfLocation = MutableLiveData<List<LocationModel>>(getListLocationsPreference())

    fun initLocationRequest(activity: AppCompatActivity) {
        locationRequest.locationRequestInit(activity)
    }

    fun updateListOfLocations(newItem: LocationModel) {
        val i = listOfLocation.value?.contains(newItem) ?: false
        if (!i) listOfLocation.value = listOfLocation.value?.plus(newItem) ?: listOf(newItem)

        Log.i("myLog updateListOfLocations", "$i")
        Log.i("myLog listOfLocation", "${listOfLocation.value}")
    }

    fun removeLocation(removeItem: LocationModel) {
        listOfLocation.value?.let {
            if (it.size > 1) {
                if (removeItem != currentLocation.value) {
                    listOfLocation.value = it.minus(removeItem)
                }
            }
        }


    }


    fun getLastLocation() {
        val coroutineHandler = CoroutineExceptionHandler { _, throwable ->
            Log.i("myLog get Location data exception", throwable.toString())
            currentLocation.postValue(null)
        }

        locationRequest.checkPermissionLocation {
            viewModelScope.launch(Dispatchers.Main + coroutineHandler) {
                currentLocation.value = it
            }

        }
    }

}