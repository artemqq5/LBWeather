package com.lbweather.myapplication.network.requestAPI

import com.lbweather.myapplication.weatherModelData.WeatherModel
import retrofit2.Response

class WeatherRepository {

    suspend fun getWeatherData(
        location: String,
        language: String
    ): Response<WeatherModel> {
        return WeatherInstance.getAPI.getWeatherData(location, language)
    }

}