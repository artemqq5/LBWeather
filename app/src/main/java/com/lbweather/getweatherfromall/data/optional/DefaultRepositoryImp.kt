package com.lbweather.getweatherfromall.data.optional

//import androidx.fragment.app.FragmentActivity
//import com.lbweather.getweatherfromall.domain.model.weather.WeatherModel
//import com.lbweather.getweatherfromall.domain.optional.DefaultRepository
//import com.lbweather.getweatherfromall.data.database.LocationTable
//import com.lbweather.getweatherfromall.data.datastore.MyStorageSettings
//import com.lbweather.getweatherfromall.domain.*
//import com.lbweather.getweatherfromall.domain.network.ConnectionManager
//import com.lbweather.getweatherfromall.presentation.SetLocationByGPS
//import kotlinx.coroutines.flow.Flow
//import retrofit2.Response
//
//class DefaultRepositoryImp(
//    private val useCaseDataLocation: UseCaseDataLocation,
//    private val useCaseCurrentLocation: UseCaseCurrentLocation,
//    private val useCaseInternet: UseCaseInternet,
//    private val useCaseLocationPermission: UseCaseLocationPermission,
//    private val useCaseSettings: MyStorageSettings
//) : DefaultRepository {
//
//    override suspend fun getLocationList(): List<LocationTable> {
//        return useCaseDataLocation.getDataLocationList()
//    }
//    override suspend fun setNewLocation(locationTable: LocationTable) {
//        useCaseDataLocation.setDataLocation(locationTable)
//    }
//    override suspend fun deleteLocation(locationTable: LocationTable) {
//        useCaseDataLocation.deleteDataLocation(locationTable)
//    }
//
//
//    override fun getCurrentLocation(): Flow<LocationTable?> {
//        return useCaseCurrentLocation.getCurrentDataLocation()
//    }
//    override suspend fun getLastCurrentLocation(): LocationTable {
//        return useCaseCurrentLocation.getLastCurrentLocation()
//    }
//    override suspend fun setCurrentLocation(locationTable: LocationTable) {
//        useCaseCurrentLocation.setCurrentLocation(locationTable)
//    }
//
//    override fun getConnectionFlow(): Flow<ConnectionManager.StatusInternet> {
//        return useCaseInternet.getConnectionFlow()
//    }
//    override fun isInternetActive(): Boolean {
//        return useCaseInternet.getStatusInternet()
//    }
//
//    override fun FragmentActivity.initPermissionRequestLauncher() {
//        useCaseLocationPermission.apply {
//            initPermissionRequestLauncher()
//        }
//    }
//    override fun checkLocationPermission() {
//        useCaseLocationPermission.checkLocationPermission()
//    }
//    override fun initCallBackSetLocationByGPS(interfaceModel: SetLocationByGPS) {
//        useCaseLocationPermission.initCallBackSetLocationByGPS(interfaceModel)
//    }
//
//    override fun getTimeFormat(): String {
//        return useCaseSettings.getTimeFormat()
//    }
//    override fun getTempUnit(): String {
//        return useCaseSettings.getTempUnit()
//    }
//
//}