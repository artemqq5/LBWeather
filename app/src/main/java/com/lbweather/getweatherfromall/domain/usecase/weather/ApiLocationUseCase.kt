package com.lbweather.getweatherfromall.domain.usecase.weather

import com.lbweather.getweatherfromall.MyApp.Companion.logData
import com.lbweather.getweatherfromall.domain.model.SearchLocation
import com.lbweather.getweatherfromall.domain.model.weather.LocationDataModel
import retrofit2.Response

class ApiLocationUseCase(private val weatherAPI: WeatherAPI) {
    suspend fun getLocationData(location: String, lang: String): Response<SearchLocation> {
        return weatherAPI.getLocationData(location, lang)
    }

}