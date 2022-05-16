package com.example.myapplication.network.requestAPI

import com.example.myapplication.weatherModelData.WeatherModel
import retrofit2.Response

class WeatherRepository {

    suspend fun getWeatherData(
        location: String,
        language: String
    ) : Response<WeatherModel> {
        return WeatherInstance.getAPI.getWeatherData(location, language)
    }

}