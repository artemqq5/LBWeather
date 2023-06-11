package com.lbweather.getweatherfromall.helper

import android.content.Context

object FromStr {

    fun Context.fromStr(id: Int): String = resources.getString(id)
    fun Context.fromArray(id: Int, position: Int): String =
        resources.getStringArray(id)[position]

}