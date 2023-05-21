package com.lbweather.myapplication.domain

import android.view.View
import com.lbweather.myapplication.MyApp.Companion.logData
import com.lbweather.myapplication.data.database.LocationTable
import com.lbweather.myapplication.data.datastore.MyStorage
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UseCaseCurrentLocation(private val myStorage: MyStorage) {

    private val moshiConverter: Moshi by lazy {
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    fun getCurrentDataLocation(): Flow<LocationTable?> {
        return myStorage.myCurrentLocation.map {
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
            val myLastCurrentLocation = myStorage.myLastCurrentLocation()
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
        myStorage.setCurrentLocation(
            moshiConverter.adapter(LocationTable::class.java).toJson(locationTable)
        )
    }

}