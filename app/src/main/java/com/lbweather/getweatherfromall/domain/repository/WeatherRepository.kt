package com.lbweather.getweatherfromall.domain.repository

import com.lbweather.getweatherfromall.domain.model.weather.WeatherDataModel
import retrofit2.Response

interface WeatherRepository {
    suspend fun getWeather(location: String, lang: String): Response<WeatherDataModel>

}