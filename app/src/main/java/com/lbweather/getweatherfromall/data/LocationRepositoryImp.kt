package com.lbweather.getweatherfromall.data

import com.lbweather.getweatherfromall.data.database.LocationDao
import com.lbweather.getweatherfromall.data.database.LocationTable
import com.lbweather.getweatherfromall.domain.model.LocationUserModel
import com.lbweather.getweatherfromall.domain.model.SearchLocation
import com.lbweather.getweatherfromall.domain.repository.LocationRepository
import com.lbweather.getweatherfromall.domain.usecase.weather.ApiLocationUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocationRepositoryImp(
    private val dao: LocationDao,
    private val apiLocationUseCase: ApiLocationUseCase
) : LocationRepository {

    override suspend fun searchLocation(location: String, lang: String): LocationUserModel? {
        return apiLocationUseCase.getLocationData(location, lang).body()?.toLocationUserModel()
    }

    override fun getAllLocation(): Flow<List<LocationUserModel>> {
        return dao.getAll().map { list -> list.map { it.toLocationUserModel() } }
    }

    override suspend fun addNewLocation(locationUserModel: LocationUserModel) {
        dao.insert(locationUserModel.toLocationTable())
    }

    override suspend fun deleteLocation(locationUserModel: LocationUserModel) {
        dao.delete(locationUserModel.toLocationTable())
    }

    override suspend fun getActiveLocation(): LocationUserModel {
        return dao.getActiveLocation()[0].toLocationUserModel()
    }

    override suspend fun setActiveLocation(locationUserModel: LocationUserModel) {
        dao.resetLocationStatus()
        dao.update(locationUserModel.toLocationTable().apply { statusActive = true })
    }

    private fun LocationTable.toLocationUserModel(): LocationUserModel {
        return LocationUserModel(
            shortLocation = this.shortLocation,
            country = this.country,
            region = this.region,
            lat = this.lat,
            lon = this.lon,
            status = this.statusActive
        )
    }

    private fun LocationUserModel.toLocationTable(): LocationTable {
        return LocationTable(
            shortLocation = this.shortLocation,
            country = this.country,
            region = this.region,
            lat = this.lat,
            lon = this.lon,
            statusActive = this.status
        )
    }

    private fun SearchLocation.toLocationUserModel(): LocationUserModel {
        return LocationUserModel(
            shortLocation = this.location.shortLocation,
            country = this.location.country,
            region = this.location.region,
            lat = this.location.lat,
            lon = this.location.lon,
            status = false
        )
    }

}