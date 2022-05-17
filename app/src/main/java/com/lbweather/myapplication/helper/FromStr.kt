package com.lbweather.myapplication.helper

import com.lbweather.myapplication.MainActivity.Companion.main_context

object FromStr {

    fun fromStr(id: Int): String = main_context.resources.getString(id)
    fun fromArray(id: Int, position: Int): String =
        main_context.resources.getStringArray(id)[position]

}