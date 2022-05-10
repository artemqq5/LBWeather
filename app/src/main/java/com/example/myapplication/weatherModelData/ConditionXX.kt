package com.example.myapplication.weatherModelData

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class ConditionXX(
    val icon: String,
    val text: String
):  Parcelable