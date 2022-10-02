package com.lbweather.myapplication.dialogs

import com.lbweather.myapplication.R
import com.lbweather.myapplication.MainActivity.Companion.main_context
import com.lbweather.myapplication.helper.FromStr.fromStr
import com.lbweather.myapplication.location.LocationModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object DialogLocation {

    val dialogLocation: (LocationModel, (LocationModel?) -> Unit) -> MaterialAlertDialogBuilder =
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