package com.lbweather.myapplication.other.dialogs

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.lbweather.myapplication.R
import com.lbweather.myapplication.MainActivity.Companion.main_context
import com.lbweather.myapplication.other.helper.FromStr.fromStr
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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
                    it.data = Uri.fromParts(
                        "package",
                        main_context.packageName,
                        null
                    )
                }

                main_context.startActivity(intent)

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
                main_context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                dialog.cancel()
            }


}