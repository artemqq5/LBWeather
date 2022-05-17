package com.lbweather.myapplication.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.checkSelfPermission
import com.lbweather.myapplication.R
import com.lbweather.myapplication.MainActivity.Companion.main_context
import com.lbweather.myapplication.dialogs.DialogLocationPermission.dialogGPS
import com.lbweather.myapplication.dialogs.DialogLocationPermission.dialogPermission
import com.lbweather.myapplication.helper.FromStr.fromStr
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

    fun checkPermissionLocation(func: (LocationModel) -> Unit) {
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
    fun getLastLocation(func: (LocationModel) -> Unit) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {

                val geocoder = Geocoder(main_context, Locale.UK)
                val finalLocale = geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )

                val locationObj = finalLocale[0]
                func(
                    LocationModel(
                        locationObj.latitude.toString(),
                        locationObj.longitude.toString(),
                        locationObj.locality.toString()
                    )
                )
            } else {
                Toast.makeText(main_context, fromStr(R.string.noDataLocation), Toast.LENGTH_SHORT)
                    .show()
            }
        }.addOnFailureListener {
            Toast.makeText(main_context, fromStr(R.string.errorGetLocation), Toast.LENGTH_SHORT)
                .show()
        }
    }

}