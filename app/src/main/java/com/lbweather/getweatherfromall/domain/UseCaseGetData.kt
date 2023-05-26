package com.lbweather.getweatherfromall.domain

import com.lbweather.getweatherfromall.domain.network.ClientHTTPS
import com.lbweather.getweatherfromall.domain.model.weather.WeatherModel
import retrofit2.Response

class UseCaseGetData(private val client: ClientHTTPS) {

    suspend fun getWeatherData(location: String): Response<WeatherModel> {
        return client.retrofitClient.getWeatherData(location)
    }

}