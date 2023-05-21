package com.lbweather.myapplication.domain

import com.lbweather.myapplication.domain.network.ClientHTTPS
import com.lbweather.myapplication.domain.model.weather.WeatherModel
import retrofit2.Response

class UseCaseGetData(private val client: ClientHTTPS) {

    suspend fun getWeatherData(location: String): Response<WeatherModel> {
        return client.retrofitClient.getWeatherData(location)
    }

}