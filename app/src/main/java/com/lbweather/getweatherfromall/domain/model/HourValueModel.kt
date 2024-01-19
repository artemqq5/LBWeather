package com.lbweather.getweatherfromall.domain.model

import com.lbweather.getweatherfromall.domain.model.weather.ConditionModel
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HourValueModel(
    val conditionModel: ConditionModel,
    val temp: Double,
    val time: String
)