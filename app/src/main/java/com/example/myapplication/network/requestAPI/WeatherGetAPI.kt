package com.example.myapplication.network.requestAPI

import com.example.myapplication.weatherModelData.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherGetAPI {

    @GET("forecast.json?key=052cd2e807fa4b7cb3e160615220304&days=3&aqi=yes&alerts=no")
    suspend fun getWeatherData(
        @Query("q") location: String,
        @Query("lang") language: String,
    ): Response<WeatherModel>

}