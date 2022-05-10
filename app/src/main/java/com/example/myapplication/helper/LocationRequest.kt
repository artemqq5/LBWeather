package com.example.myapplication.helper

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.checkSelfPermission
import com.example.myapplication.MainActivity.Companion.main_context
import com.example.myapplication.dialogs.DialogLocationPermission.dialogGPS
import com.example.myapplication.dialogs.DialogLocationPermission.dialogPermission
import com.google.android.gms.location.LocationServices
import java.util.*


object LocationRequest {

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(main_context)
    }

    private val enabled by lazy {
        main_context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
    }

    private lateinit var locationPermissionRequest: ActivityResultLauncher<String>

    fun locationRequestInit() {
        locationPermissionRequest = main_context.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { permission ->

            if (permission) {

                if (!isActiveGPS()) {
                    dialogGPS.show()
                }

            } else {
                dialogPermission.show()
            }

        }
    }

    fun checkPermissionLocation(func: (String) -> Unit) {
        if (checkSelfPermission(
                main_context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            if (isActiveGPS()) {
                getLastLocation(func)

            } else {
                dialogGPS.show()
            }

        } else {
            locationPermissionRequest.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    private fun isActiveGPS(): Boolean {
        return enabled.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation(func: (String) -> Unit) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {

                val geocoder = Geocoder(main_context, Locale.getDefault())
                val finalLocale = geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )

                Log.i("tytgyhu3j2", "fusedLocationClient ${finalLocale[0].locality}")

                func(finalLocale[0].locality)
            } else {
                Log.i("tytgyhu3j2", "fusedLocationClient is null")
            }
        }
    }

}