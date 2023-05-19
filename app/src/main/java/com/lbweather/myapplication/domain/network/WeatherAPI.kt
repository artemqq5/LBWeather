package com.lbweather.myapplication.domain.network

import com.lbweather.myapplication.BuildConfig
import com.lbweather.myapplication.domain.model.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("forecast.json?key=${BuildConfig.API_KEY}&days=3&aqi=yes&alerts=no")
    suspend fun getWeatherData(
        @Query("q") location: String = "Kiev",
        @Query("lang") language: String = "en",
    ): Response<WeatherModel>
}