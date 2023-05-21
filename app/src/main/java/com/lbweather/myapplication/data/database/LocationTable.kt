package com.lbweather.myapplication.data.database

import android.view.View
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lbweather.myapplication.data.listOfUkraineCity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "locationTable")
open class LocationTable(
    @PrimaryKey @ColumnInfo(name = "region") open val region: String,
    @ColumnInfo(name = "lat") open val lat: Double,
    @ColumnInfo(name = "country") open val country: String,
    @ColumnInfo(name = "localtime_epoch") open val localtime_epoch: Int,
    @ColumnInfo(name = "lon") open val lon: Double,
    @ColumnInfo(name = "name") open val name: String,
    @ColumnInfo(name = "tz_id") open val tz_id: String,
    @ColumnInfo(name = "localtime") open val localtime: String,
    @ColumnInfo(name = "status_active") var statusActive: Int = View.INVISIBLE,
) {
    open val locationField: String
        get() = "$name, ${if (name in listOfUkraineCity) "Ukraine" else country}"
}