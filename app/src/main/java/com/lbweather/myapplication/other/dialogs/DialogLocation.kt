package com.lbweather.myapplication.other.dialogs

import android.content.Context
import com.lbweather.myapplication.R
import com.lbweather.myapplication.other.helper.FromStr.fromStr
import com.google.android.material.dialog.MaterialAlertDialogBuilder

//object DialogLocation {
//
//    val Context.dialogLocation: (LocationModel, (LocationModel?) -> Unit) -> MaterialAlertDialogBuilder
//        get() = { location, requestFunction ->
//            MaterialAlertDialogBuilder(this)
//                .setTitle(fromStr(R.string.location))
//                .setMessage("${fromStr(R.string.youFrom)} ${location.locality}?")
//                .setNegativeButton(fromStr(R.string.no)) { dialog, _ ->
//                    dialog.cancel()
//                }
//                .setPositiveButton(fromStr(R.string.yes)) { dialog, _ ->
//                    requestFunction(location)
//                    dialog.cancel()
//                }
//
//        }
//
//}