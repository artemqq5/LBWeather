package com.lbweather.myapplication.network.requestAPI

import com.lbweather.myapplication.BuildConfig
import com.lbweather.myapplication.weatherModelData.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherGetAPI {

    @GET("forecast.json?key=${BuildConfig.API_KEY}&days=3&aqi=yes&alerts=no")
    suspend fun getWeatherData(
        @Query("q") location: String,
        @Query("lang") language: String,
    ): Response<WeatherModel>

}