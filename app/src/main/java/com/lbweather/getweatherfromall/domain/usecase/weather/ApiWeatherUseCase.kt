package com.lbweather.getweatherfromall.domain.usecase.weather

import com.lbweather.getweatherfromall.domain.model.weather.WeatherDataModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ApiWeatherUseCase {

    companion object {
        private const val BASE_URL = "https://api.weatherapi.com/v1/"
    }

    private val moshi: Moshi by lazy {
        Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    private val weatherApi: WeatherAPI by lazy {
        Retrofit
            .Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .client(OkHttpClient())
            .build()
            .create(WeatherAPI::class.java)
    }

    suspend fun getWeatherData(location: String, lang: String): Response<WeatherDataModel> {
        return weatherApi.getWeatherData(location, lang)
    }

}