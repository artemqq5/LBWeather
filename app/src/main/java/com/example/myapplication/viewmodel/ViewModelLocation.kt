package com.example.myapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.helper.locationModel.LocationModel

class ViewModelLocation : ViewModel() {


    val currentLocation by lazy {
        MutableLiveData<LocationModel>()
    }

    fun updateCurrentLocation(loc: LocationModel) {
        currentLocation.value = loc
    }


}