package com.lbweather.getweatherfromall.domain.usecase

//import android.Manifest
//import android.annotation.SuppressLint
//import android.content.Context
//import android.content.pm.PackageManager
//import android.location.Geocoder
//import android.location.LocationManager
//import android.os.Build
//import androidx.activity.result.ActivityResultLauncher
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.ContextCompat.checkSelfPermission
//import androidx.fragment.app.FragmentActivity
//import com.google.android.gms.location.LocationServices
//import com.lbweather.getweatherfromall.MyApp.Companion.logData
//import com.lbweather.getweatherfromall.presentation.DialogLocationPermission.dialogGPS
//import com.lbweather.getweatherfromall.presentation.DialogLocationPermission.dialogPermission
//import java.util.*
//
//
//class LocationPermissionUseCase(private val context: Context) {
//
//    private val fusedLocationClient by lazy {
//        LocationServices.getFusedLocationProviderClient(context)
//    }
//
//    private val enabled by lazy {
//        context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
//    }
//
//    private var permissionRequestLauncher: ActivityResultLauncher<String>? = null
//    private var setLocationByGPS: SetLocationByGPS? = null
//
//    fun initPermissionRequestLauncher(f: FragmentActivity) {
//        permissionRequestLauncher = f.registerForActivityResult(
//            ActivityResultContracts.RequestPermission()
//        ) { permission ->
//            if (permission) {
//                logData("permission is success")
//
//                if (!isActiveGPS()) {
//                    logData("gps is not active")
//                    f.dialogGPS.show()
//                } else {
//                    logData("gps is active")
//                }
//
//            } else {
//                logData("permission is not success")
//                f.dialogPermission.show()
//            }
//
//        }
//    }
//
//    fun initCallBackSetLocationByGPS(interfaceModel: SetLocationByGPS) {
//        setLocationByGPS = interfaceModel
//    }
//
//    fun checkPermissionLocation() {
//        if (checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            if (isActiveGPS()) {
//                logData("gps is active")
//                getLastLocation()
//            } else {
//                logData("gps is not active")
//                context.dialogGPS.show()
//            }
//
//        } else {
//            permissionRequestLauncher?.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
//        }
//    }
//
//    private fun isActiveGPS(): Boolean {
//        return enabled.isProviderEnabled(LocationManager.GPS_PROVIDER)
//    }
//
//    @SuppressLint("MissingPermission")
//    private fun getLastLocation() {
//        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
//            if (location != null) {
//                logData("location is $location")
//
//                val geocoder = Geocoder(context, Locale.UK)
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                    geocoder.getFromLocation(location.latitude, location.longitude, 1) {
//                        logData(it[0].locality)
//                        setLocationByGPS?.setGSPLocation(it[0].locality)
//                    }
//                } else {
//                    @Suppress("DEPRECATION")
//                    geocoder.getFromLocation(location.latitude, location.longitude, 1)?.let {
//                        logData(it[0].locality)
//                        setLocationByGPS?.setGSPLocation(it[0].locality)
//                    }
//                }
//
//            } else {
//                logData("location is null")
//                setLocationByGPS?.setGSPLocation(null)
//            }
//        }.addOnFailureListener {
//            setLocationByGPS?.setGSPLocation(null)
//            logData("error getting location")
//        }
//    }
//
//}