package com.lbweather.getweatherfromall.domain.optional

import androidx.fragment.app.FragmentActivity
import com.lbweather.getweatherfromall.domain.optional.network.ConnectionManager
import com.lbweather.getweatherfromall.presentation.SetLocationByGPS
import kotlinx.coroutines.flow.Flow

interface DefaultRepository {

    fun getConnectionFlow(): Flow<ConnectionManager.StatusInternet>
    fun isInternetActive(): Boolean

    fun FragmentActivity.initPermissionRequestLauncher()
    fun checkLocationPermission()
    fun initCallBackSetLocationByGPS(interfaceModel: SetLocationByGPS)

    fun getTimeFormat(): String
    fun getTempUnit(): String
}