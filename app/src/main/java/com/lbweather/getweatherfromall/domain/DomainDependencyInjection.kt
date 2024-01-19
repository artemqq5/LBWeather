package com.lbweather.getweatherfromall.domain

import com.lbweather.getweatherfromall.domain.usecase.ConnectionManagerUseCase
import com.lbweather.getweatherfromall.domain.usecase.DateTimeUseCase
import com.lbweather.getweatherfromall.domain.usecase.GoogleAdsUseCase
import com.lbweather.getweatherfromall.domain.usecase.weather.ApiLocationUseCase
import com.lbweather.getweatherfromall.domain.usecase.weather.ApiWeatherUseCase
import com.lbweather.getweatherfromall.domain.usecase.weather.WeatherAPI
import com.lbweather.getweatherfromall.domain.usecase.weather.WeatherAPI.Companion.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val domainDI = module {

    single<Moshi> {
        Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    single<WeatherAPI> {
        Retrofit
            .Builder()
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .baseUrl(BASE_URL)
            .client(OkHttpClient())
            .build()
            .create(WeatherAPI::class.java)
    }

    factory { ApiWeatherUseCase(weatherAPI = get()) }
    factory { ApiLocationUseCase(weatherAPI = get()) }
    factory { GoogleAdsUseCase() }
    factory { DateTimeUseCase() }
    factory { ConnectionManagerUseCase(context = get()) }

}
