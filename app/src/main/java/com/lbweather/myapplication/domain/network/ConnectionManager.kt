package com.lbweather.myapplication.domain.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class ConnectionManager(context: Context) {
    enum class StatusInternet {
        Lost, Available
    }

    private val connectiveManager =
        context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager

    private var networkListener: ConnectivityManager.NetworkCallback? = null

    fun changeStateConnection(): Flow<StatusInternet> {
        return callbackFlow {
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    launch {
                        send(StatusInternet.Available)
                    }
                }

                override fun onLost(network: Network) {
                    launch {
                        if (!isInternet()) {
                            send(StatusInternet.Lost)
                        }
                    }
                }
            }.also {
                connectiveManager.registerDefaultNetworkCallback(it)
            }

            awaitClose {
                networkListener?.let {
                    connectiveManager.unregisterNetworkCallback(it)
                }
            }
        }.distinctUntilChanged()

    }

    fun isInternet(): Boolean {
        val network = connectiveManager.activeNetwork
        val activeNetwork = connectiveManager.getNetworkCapabilities(network) ?: return false
        return (activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || activeNetwork.hasTransport(
            NetworkCapabilities.TRANSPORT_CELLULAR
        ))
    }
}