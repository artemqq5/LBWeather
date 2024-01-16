package com.lbweather.getweatherfromall.domain.optional

import com.lbweather.getweatherfromall.domain.optional.network.ConnectionManager
import kotlinx.coroutines.flow.Flow

class UseCaseInternet(private val connectionManager: ConnectionManager) {

    fun getConnectionFlow(): Flow<ConnectionManager.StatusInternet> {
        return connectionManager.changeStateConnection()
    }

    fun getStatusInternet(): Boolean {
        return connectionManager.isInternet()
    }

}