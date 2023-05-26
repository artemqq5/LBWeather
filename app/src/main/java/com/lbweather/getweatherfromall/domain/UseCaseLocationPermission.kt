package com.lbweather.getweatherfromall.domain

import androidx.fragment.app.FragmentActivity
import com.lbweather.getweatherfromall.domain.location_permission.LocationRequest
import com.lbweather.getweatherfromall.presentation.locationsFragment.SetLocationByGPS


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