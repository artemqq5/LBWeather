package com.lbweather.myapplication.data

import com.lbweather.myapplication.domain.repository.DefaultRepository
import com.lbweather.myapplication.domain.UseCaseGetData
import com.lbweather.myapplication.domain.model.WeatherModel
import retrofit2.Response

class DefaultRepositoryImp(private val useCaseGetData: UseCaseGetData) : DefaultRepository {

    override suspend fun getWeather(): Response<WeatherModel> {
        return useCaseGetData.getWeatherData()
    }


}