package com.lbweather.getweatherfromall.presentation

import com.lbweather.getweatherfromall.presentation.viewmodel.LocationViewModel
import com.lbweather.getweatherfromall.presentation.viewmodel.PreferenceViewModel
import com.lbweather.getweatherfromall.presentation.viewmodel.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationDI = module {

    viewModel {
        WeatherViewModel(weatherRepository = get())
    }

    viewModel {
        LocationViewModel(locationRepository = get())
    }

    viewModel {
        PreferenceViewModel(preferenceRepository = get())
    }

}