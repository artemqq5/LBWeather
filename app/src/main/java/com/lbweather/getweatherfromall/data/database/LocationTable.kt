package com.lbweather.getweatherfromall.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "locationTable")
class LocationTable(
    @PrimaryKey @ColumnInfo(name = "region") val region: String,
    @ColumnInfo(name = "lat") val lat: Double,
    @ColumnInfo(name = "lon") val lon: Double,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "short_location") val shortLocation: String,
    @ColumnInfo(name = "status") var statusActive: Boolean = false,
)