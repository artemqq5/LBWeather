package com.lbweather.myapplication.network.requestAPI

import android.content.Context
import androidx.core.content.ContextCompat
import com.lbweather.myapplication.R
import com.lbweather.myapplication.weatherModelData.WeatherModel
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val api: WeatherGetAPI
) {

    suspend fun getWeatherData(
        location: String,
        language: String
    ): Response<WeatherModel> {
        return api.getWeatherData(location, language)
    }

}