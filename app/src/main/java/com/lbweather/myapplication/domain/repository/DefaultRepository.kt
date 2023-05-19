package com.lbweather.myapplication.domain.repository

import com.lbweather.myapplication.domain.model.WeatherModel
import retrofit2.Response

interface DefaultRepository {

    suspend fun getWeather(): Response<WeatherModel>

}