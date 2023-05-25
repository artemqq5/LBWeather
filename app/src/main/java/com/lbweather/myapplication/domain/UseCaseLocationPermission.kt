package com.lbweather.myapplication.domain

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.lbweather.myapplication.domain.location_permission.LocationRequest
import com.lbweather.myapplication.presentation.locationsFragment.SetLocationByGPS


class UseCaseLocationPermission(private val locationRequest: LocationRequest) {

    fun FragmentActivity.initPermissionRequestLauncher() {
        locationRequest.apply {
            initPermissionRequestLauncher()
        }
    }

    fun initCallBackSetLocationByGPS(interfaceModel: SetLocationByGPS) {
        locationRequest.initCallBackSetLocationByGPS(interfaceModel)
    }

    fun checkLocationPermission() {
        locationRequest.checkPermissionLocation()
    }

}