package com.lbweather.myapplication.other.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.checkSelfPermission
import com.lbweather.myapplication.R
import com.lbweather.myapplication.other.dialogs.DialogLocationPermission.dialogGPS
import com.lbweather.myapplication.other.dialogs.DialogLocationPermission.dialogPermission
import com.lbweather.myapplication.other.helper.FromStr.fromStr
import com.google.android.gms.location.LocationServices
import java.util.*


class LocationRequest (private val context: Context) {

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    private val enabled by lazy {
        context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
    }

    private lateinit var locationPermissionRequest: ActivityResultLauncher<String>

    fun locationRequestInit(activity: AppCompatActivity) {
        locationPermissionRequest = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { permission ->

            if (permission) {

                if (!isActiveGPS()) {
                    context.dialogGPS.show()
                }

            } else {
                context.dialogPermission.show()
            }

        }
    }

    fun checkPermissionLocation(func: (LocationModel) -> Unit) {
        if (checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            if (isActiveGPS()) {
                getLastLocation(func)

            } else {
                context.dialogGPS.show()
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

                val geocoder = Geocoder(context, Locale.UK)
                val finalLocale = geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )

                val locationObj = finalLocale?.get(0)
                func(
                    LocationModel(
                        locationObj?.latitude.toString(),
                        locationObj?.longitude.toString(),
                        locationObj?.locality.toString()
                    )
                )
            } else {
                Toast.makeText(context, context.fromStr(R.string.noDataLocation), Toast.LENGTH_SHORT)
                    .show()
            }
        }.addOnFailureListener {
            Toast.makeText(context, context.fromStr(R.string.errorGetLocation), Toast.LENGTH_SHORT)
                .show()
        }
    }

}