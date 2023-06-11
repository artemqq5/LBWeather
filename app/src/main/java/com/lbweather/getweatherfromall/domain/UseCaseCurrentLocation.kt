package com.lbweather.getweatherfromall.domain

import com.lbweather.getweatherfromall.MyApp.Companion.logData
import com.lbweather.getweatherfromall.data.database.LocationTable
import com.lbweather.getweatherfromall.data.datastore.MyStorageLocation
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UseCaseCurrentLocation(private val myStorageLocation: MyStorageLocation) {

    private val moshiConverter: Moshi by lazy {
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    fun getCurrentDataLocation(): Flow<LocationTable?> {
        return myStorageLocation.myCurrentLocation.map {
            try {
                logData("getCurrentDataLocation() $it")
                moshiConverter.adapter(LocationTable::class.java).fromJson((it))
            } catch (e: Exception) {
                logData("getCurrentDataLocation() error: $e")
                LocationTable(
                    country = "Ukraine",
                    lat = 50.43,
                    localtime = "2023-05-21 14:47",
                    localtime_epoch = 1684669654,
                    lon = 30.52,
                    name = "Kiev",
                    region = "Kyyivs'ka Oblast'",
                    tz_id = "Europe/Kiev"
                )
            }
        }
    }

    suspend fun getLastCurrentLocation(): LocationTable {
        return try {
            val myLastCurrentLocation = myStorageLocation.myLastCurrentLocation()
            logData("getLastCurrentLocation() $myLastCurrentLocation")
            moshiConverter.adapter(LocationTable::class.java)
                .fromJson((myLastCurrentLocation))!!
        } catch (e: Exception) {
            logData("getLastCurrentLocation() error: $e")
            LocationTable(
                country = "Ukraine",
                lat = 50.43,
                localtime = "2023-05-21 14:47",
                localtime_epoch = 1684669654,
                lon = 30.52,
                name = "Kiev",
                region = "Kyyivs'ka Oblast'",
                tz_id = "Europe/Kiev"
            )
        }
    }

    suspend fun setCurrentLocation(locationTable: LocationTable) {
        myStorageLocation.setCurrentLocation(
            moshiConverter.adapter(LocationTable::class.java).toJson(locationTable)
        )
    }

}