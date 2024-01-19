package com.lbweather.getweatherfromall.domain.usecase.weather

import com.lbweather.getweatherfromall.BuildConfig
import com.lbweather.getweatherfromall.domain.model.SearchLocation
import com.lbweather.getweatherfromall.domain.model.weather.LocationDataModel
import com.lbweather.getweatherfromall.domain.model.weather.WeatherDataModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    companion object {
        const val BASE_URL = "https://api.weatherapi.com/v1/"
    }

    @GET("forecast.json?key=${BuildConfig.API_KEY}&days=3")
    suspend fun getWeatherData(
        @Query("q") location: String,
        @Query("lang") language: String,
    ): Response<WeatherDataModel>

    @GET("timezone.json?key=${BuildConfig.API_KEY}")
    suspend fun getLocationData(
        @Query("q") location: String,
        @Query("lang") language: String,
    ): Response<SearchLocation>

}