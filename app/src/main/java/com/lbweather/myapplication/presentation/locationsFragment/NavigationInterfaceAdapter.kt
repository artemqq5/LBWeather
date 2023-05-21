package com.lbweather.myapplication.presentation.locationsFragment

import com.lbweather.myapplication.data.database.LocationTable

interface NavigationInterfaceAdapter {

    fun changeCurrentLocation(location: LocationTable)

}