package com.lbweather.getweatherfromall.domain.usecase.weather

import com.lbweather.getweatherfromall.domain.model.weather.LocationDataModel
import com.lbweather.getweatherfromall.domain.model.weather.WeatherDataModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ApiWeatherUseCase(private val weatherAPI: WeatherAPI) {

    suspend fun getWeatherData(location: String, lang: String): Response<WeatherDataModel> {
        return weatherAPI.getWeatherData(location, lang)
    }

}