package com.lbweather.getweatherfromall.domain

import com.lbweather.getweatherfromall.data.database.LocationTable
import com.lbweather.getweatherfromall.data.database.MyDataBase

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