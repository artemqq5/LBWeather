package com.lbweather.myapplication.network.internetConnection

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.lbweather.myapplication.MainActivity.Companion.main_context


fun Context.checkForInternet(): Boolean {

    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

        // else return false
        else -> false
    }
}