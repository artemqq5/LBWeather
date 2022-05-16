package com.example.myapplication.helper

import com.example.myapplication.MainActivity.Companion.main_context

object FromStr {

    fun fromStr(id: Int): String = main_context.resources.getString(id)
    fun fromArray(id: Int, position: Int): String =
        main_context.resources.getStringArray(id)[position]

}