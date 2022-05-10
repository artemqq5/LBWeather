package com.example.myapplication.dialogs

import com.example.myapplication.MainActivity
import com.example.myapplication.helper.locationModel.LocationModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object DialogLocation {

    val dialogLocation: (LocationModel, (LocationModel) -> Unit) -> MaterialAlertDialogBuilder = { location, requestFunction ->
        MaterialAlertDialogBuilder(MainActivity.main_context)
            .setTitle("Локація")
            .setMessage("Ти з ${location.locality}?")
            .setNegativeButton("Ні") { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton("Так") { dialog, _ ->
                requestFunction(location)
                dialog.cancel()
            }

    }

}