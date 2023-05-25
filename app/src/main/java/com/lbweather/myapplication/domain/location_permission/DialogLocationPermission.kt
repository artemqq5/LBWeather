package com.lbweather.myapplication.domain.location_permission

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lbweather.myapplication.BuildConfig
import com.lbweather.myapplication.R
import com.lbweather.myapplication.other.helper.FromStr.fromStr

object DialogLocationPermission {

    val Context.dialogPermission: MaterialAlertDialogBuilder
        get() = MaterialAlertDialogBuilder(this)
            .setTitle(fromStr(R.string.noPermission))
            .setMessage(fromStr(R.string.allowLocation))
            .setNegativeButton(fromStr(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(fromStr(R.string.yes)) { dialog, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also {
                    it.data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                }
                startActivity(intent)
                dialog.cancel()
            }

    val Context.dialogGPS: MaterialAlertDialogBuilder
        get() = MaterialAlertDialogBuilder(this)
            .setTitle(fromStr(R.string.onGPS))
            .setMessage(fromStr(R.string.whyGPS))
            .setNegativeButton(fromStr(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(fromStr(R.string.yes)) { dialog, _ ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                dialog.cancel()
            }


}