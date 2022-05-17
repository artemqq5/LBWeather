package com.lbweather.myapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lbweather.myapplication.location.LocationModel

class ViewModelLocation : ViewModel() {


    val currentLocation by lazy {
        MutableLiveData<LocationModel>()
    }

    fun updateCurrentLocation(loc: LocationModel) {
        currentLocation.value = loc
    }


}