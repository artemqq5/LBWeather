package com.lbweather.getweatherfromall.domain

import com.lbweather.getweatherfromall.domain.network.ConnectionManager
import kotlinx.coroutines.flow.Flow

class UseCaseInternet(private val connectionManager: ConnectionManager) {

    fun getConnectionFlow(): Flow<ConnectionManager.StatusInternet> {
        return connectionManager.changeStateConnection()
    }

    fun getStatusInternet(): Boolean {
        return connectionManager.isInternet()
    }

}