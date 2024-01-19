package com.lbweather.getweatherfromall.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lbweather.getweatherfromall.BuildConfig
import com.lbweather.getweatherfromall.R

object DialogLocationPermission {

    val Context.dialogPermission: MaterialAlertDialogBuilder
        get() = MaterialAlertDialogBuilder(this)
            .setTitle(this.resources.getString(R.string.noPermission))
            .setMessage(this.resources.getString(R.string.allowLocation))
            .setNegativeButton(this.resources.getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(this.resources.getString(R.string.yes)) { dialog, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also {
                    it.data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                }
                startActivity(intent)
                dialog.cancel()
            }

    val Context.dialogGPS: MaterialAlertDialogBuilder
        get() = MaterialAlertDialogBuilder(this)
            .setTitle(this.resources.getString(R.string.onGPS))
            .setMessage(this.resources.getString(R.string.whyGPS))
            .setNegativeButton(this.resources.getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(this.resources.getString(R.string.yes)) { dialog, _ ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                dialog.cancel()
            }


}