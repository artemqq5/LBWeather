package com.example.myapplication.network


import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object WeatherInstance {

    private const val BASE_URL = "https://api.weatherapi.com/v1/"

    // moshi builder
    private val moshi by lazy {
        Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    // retrofit builder with convertor moshi
    private val retrofit by lazy {
        Retrofit
            .Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .build()
    }



    // lazy create model request with interface
    val getAPI: WeatherGetAPI by lazy {
        retrofit.create(WeatherGetAPI::class.java)
    }
}