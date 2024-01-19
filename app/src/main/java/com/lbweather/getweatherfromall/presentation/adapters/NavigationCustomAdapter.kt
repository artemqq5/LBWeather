package com.lbweather.getweatherfromall.presentation.adapters

import com.lbweather.getweatherfromall.domain.model.LocationUserModel

interface NavigationCustomAdapter {
    fun changeCurrentLocation(location: LocationUserModel)
}