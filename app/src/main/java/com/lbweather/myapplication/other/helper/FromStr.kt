package com.lbweather.myapplication.other.helper

import android.content.Context
import com.lbweather.myapplication.MainActivity.Companion.main_context

object FromStr {

    fun Context.fromStr(id: Int): String = resources.getString(id)
    fun fromArray(id: Int, position: Int): String =
        main_context.resources.getStringArray(id)[position]

}