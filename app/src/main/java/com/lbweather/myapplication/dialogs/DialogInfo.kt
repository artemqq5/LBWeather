package com.lbweather.myapplication.dialogs

import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lbweather.myapplication.MainActivity
import com.lbweather.myapplication.R
import com.lbweather.myapplication.helper.FromStr
import com.lbweather.myapplication.location.LocationModel

object DialogInfo {

    val dialogInfo: (() -> Unit) -> MaterialAlertDialogBuilder =
        { callback ->
            MaterialAlertDialogBuilder(MainActivity.main_context)
                .setTitle(FromStr.fromStr(R.string.info))
                .setMessage(FromStr.fromStr(R.string.infoMessage))
                .setPositiveButton(FromStr.fromStr(R.string.yes)) { dialog, _ ->
                    callback()
                    dialog.cancel()
                }

        }

}