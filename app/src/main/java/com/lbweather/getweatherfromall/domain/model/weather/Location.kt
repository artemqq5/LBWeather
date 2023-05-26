package com.lbweather.getweatherfromall.domain.model.weather

import com.lbweather.getweatherfromall.data.database.LocationTable
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Location(
    override val country: String,
    override val lat: Double,
    override val localtime: String,
    override val localtime_epoch: Int,
    override val lon: Double,
    override val name: String,
    override val region: String,
    override val tz_id: String,
) : LocationTable(country, lat, localtime, localtime_epoch, lon, name, region, tz_id) {
    override val locationField: String
        get() = super.locationField

//    fun Location.toLocationTable(): LocationTable {
//        return LocationTable(
//            this.region,
//            this.lat,
//            this.country,
//            this.localtime_epoch,
//            this.lon,
//            this.name,
//            this.tz_id,
//            this.localtime
//        )
//    }
}