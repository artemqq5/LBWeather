package com.lbweather.getweatherfromall.data

import com.lbweather.getweatherfromall.domain.model.weather.WeatherDataModel
import com.lbweather.getweatherfromall.domain.repository.WeatherRepository
import com.lbweather.getweatherfromall.domain.usecase.weather.ApiWeatherUseCase
import retrofit2.Response

class WeatherRepositoryImp(private val apiWeatherUseCase: ApiWeatherUseCase) : WeatherRepository {

    override suspend fun getWeather(location: String, lang: String): Response<WeatherDataModel> {
        return apiWeatherUseCase.getWeatherData(location, lang)
    }


}