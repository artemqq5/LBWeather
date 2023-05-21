package com.lbweather.myapplication.domain

import com.lbweather.myapplication.data.database.LocationTable
import com.lbweather.myapplication.data.database.MyDataBase

class UseCaseDataLocation(private val myDataBase: MyDataBase) {

    suspend fun getDataLocationList(): List<LocationTable> {
        return myDataBase.locationDao().getAll()
    }

    suspend fun setDataLocation(location: LocationTable) {
        myDataBase.locationDao().insert(location)
    }

    suspend fun deleteDataLocation(location: LocationTable) {
        myDataBase.locationDao().delete(location)
    }

}