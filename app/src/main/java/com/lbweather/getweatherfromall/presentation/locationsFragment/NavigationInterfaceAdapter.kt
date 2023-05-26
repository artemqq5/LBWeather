package com.lbweather.getweatherfromall.presentation.locationsFragment

import com.lbweather.getweatherfromall.data.database.LocationTable

interface NavigationInterfaceAdapter {

    fun changeCurrentLocation(location: LocationTable)

}