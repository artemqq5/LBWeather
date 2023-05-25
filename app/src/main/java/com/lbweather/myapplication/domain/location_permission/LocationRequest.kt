package com.lbweather.myapplication.domain.location_permission

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.LocationServices
import com.lbweather.myapplication.MyApp.Companion.logData
import com.lbweather.myapplication.domain.location_permission.DialogLocationPermission.dialogGPS
import com.lbweather.myapplication.domain.location_permission.DialogLocationPermission.dialogPermission
import com.lbweather.myapplication.presentation.locationsFragment.SetLocationByGPS
import java.util.*


class LocationRequest(private val context: Context) {

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    private val enabled by lazy {
        context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
    }

    private lateinit var permissionRequestLauncher: ActivityResultLauncher<String>
    private lateinit var setLocationByGPS: SetLocationByGPS

    fun FragmentActivity.initPermissionRequestLauncher() {
        permissionRequestLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { permission ->
            if (permission) {
                logData("permission is success")

                if (!isActiveGPS()) {
                    logData("gps is not active")
                    dialogGPS.show()
                }

            } else {
                logData("permission is not success")
                dialogPermission.show()
            }

        }
    }

    fun initCallBackSetLocationByGPS(interfaceModel: SetLocationByGPS) {
        setLocationByGPS = interfaceModel
    }

    fun checkPermissionLocation() {
        if (checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            if (isActiveGPS()) {
                logData("gps is active")
                getLastLocation()
            } else {
                logData("gps is not active")
                context.dialogGPS.show()
            }

        } else {
            permissionRequestLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    private fun isActiveGPS(): Boolean {
        return enabled.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                logData("location is $location")

                val geocoder = Geocoder(context, Locale.UK)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(location.latitude, location.longitude, 1) {
                        logData(it[0].locality)
                        setLocationByGPS.setGSPLocation(it[0].locality)
                    }
                } else {
                    @Suppress("DEPRECATION")
                    geocoder.getFromLocation(location.latitude, location.longitude, 1)?.let {
                        logData(it[0].locality)
                        setLocationByGPS.setGSPLocation(it[0].locality)
                    }
                }

            } else {
                logData("location is null")
                setLocationByGPS.setGSPLocation(null)
            }
        }.addOnFailureListener {
            setLocationByGPS.setGSPLocation(null)
            logData("error getting location")
        }
    }

}