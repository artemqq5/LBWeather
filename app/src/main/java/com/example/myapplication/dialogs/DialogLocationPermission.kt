package com.example.myapplication.dialogs

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.example.myapplication.MainActivity.Companion.main_context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object DialogLocationPermission {

    val dialogPermission = MaterialAlertDialogBuilder(main_context)
        .setTitle("Немає дозволу")
        .setMessage("Дозвольте вікористовувати локацію щоб визначити ваше місце положення")
        .setNegativeButton("Ні") { dialog, _ ->
            dialog.cancel()
        }
        .setPositiveButton("Так") { dialog, _ ->
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
        .setTitle("Увімкніть GPS")
        .setMessage("Щоб автоматично визначити ваше місце пложення увімкніть GPS")
        .setNegativeButton("Ні") { dialog, _ ->
            dialog.cancel()
        }
        .setPositiveButton("Так") { dialog, _ ->
            main_context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            dialog.cancel()
        }


}