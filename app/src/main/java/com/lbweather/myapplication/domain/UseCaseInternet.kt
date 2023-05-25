package com.lbweather.myapplication.domain

import com.lbweather.myapplication.domain.network.ConnectionManager
import kotlinx.coroutines.flow.Flow

class UseCaseInternet(private val connectionManager: ConnectionManager) {

    fun getConnectionFlow(): Flow<ConnectionManager.StatusInternet> {
        return connectionManager.changeStateConnection()
    }

    fun getStatusInternet(): Boolean {
        return connectionManager.isInternet()
    }

}