package com.lbweather.getweatherfromall.data

import com.lbweather.getweatherfromall.domain.repository.WeatherRepository
import org.koin.dsl.module

val dataDI = module {

    single<WeatherRepository> {
        WeatherRepositoryImp(apiWeatherUseCase = get())
    }

}