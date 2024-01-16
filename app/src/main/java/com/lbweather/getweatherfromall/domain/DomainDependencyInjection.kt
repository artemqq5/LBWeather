package com.lbweather.getweatherfromall.domain

import com.lbweather.getweatherfromall.domain.usecase.weather.ApiWeatherUseCase
import org.koin.dsl.module

val domainDI = module {

    factory { ApiWeatherUseCase() }

}