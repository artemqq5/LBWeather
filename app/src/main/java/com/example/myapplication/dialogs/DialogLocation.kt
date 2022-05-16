package com.example.myapplication.dialogs

import com.example.myapplication.MainActivity.Companion.main_context
import com.example.myapplication.R
import com.example.myapplication.helper.FromStr
import com.example.myapplication.helper.FromStr.fromStr
import com.example.myapplication.location.LocationModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object DialogLocation {

    val dialogLocation: (LocationModel, (LocationModel) -> Unit) -> MaterialAlertDialogBuilder =
        { location, requestFunction ->
            MaterialAlertDialogBuilder(main_context)
                .setTitle(fromStr(R.string.location))
                .setMessage("${fromStr(R.string.youFrom)} ${location.locality}?")
                .setNegativeButton(fromStr(R.string.no)) { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton(fromStr(R.string.yes)) { dialog, _ ->
                    requestFunction(location)
                    dialog.cancel()
                }

        }

}