package com.lbweather.myapplication.domain.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ClientHTTPS {

    companion object {
        private const val BASE_URL = "https://api.weatherapi.com/v1/"
    }

    private val moshi: Moshi by lazy {
        Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    val retrofitClient: WeatherAPI by lazy {
        Retrofit
            .Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .build()
            .create(WeatherAPI::class.java)
    }

}