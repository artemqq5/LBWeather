package com.example.myapplication.dialogs

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.example.myapplication.MainActivity.Companion.main_context
import com.example.myapplication.R
import com.example.myapplication.helper.FromStr.fromStr
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object DialogLocationPermission {

    val dialogPermission = MaterialAlertDialogBuilder(main_context)
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

    val dialogGPS = MaterialAlertDialogBuilder(main_context)
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