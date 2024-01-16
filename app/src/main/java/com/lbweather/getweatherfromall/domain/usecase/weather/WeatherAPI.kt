package com.lbweather.getweatherfromall.domain.usecase.weather

import com.lbweather.getweatherfromall.BuildConfig
import com.lbweather.getweatherfromall.domain.model.weather.WeatherDataModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("forecast.json?key=${BuildConfig.API_KEY}&days=3&aqi=yes&alerts=no")
    suspend fun getWeatherData(
        @Query("q") location: String,
        @Query("lang") language: String,
    ): Response<WeatherDataModel>
}