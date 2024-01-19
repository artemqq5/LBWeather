package com.lbweather.getweatherfromall.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lbweather.getweatherfromall.MyApp
import com.lbweather.getweatherfromall.domain.model.weather.WeatherDataModel
import com.lbweather.getweatherfromall.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class WeatherViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {

    companion object {
        const val DEFAULT_LOCATION = "London"
    }

    private val excHandler = CoroutineExceptionHandler { _, throwable ->
        MyApp.logData("Coroutine Exception. WeatherViewModel ($throwable)")
    }

    private val _weatherDataFlow = MutableSharedFlow<WeatherDataModel>(replay = 1)
    val weatherDataFlow = _weatherDataFlow.asSharedFlow()

    fun updateWeather(location: String = DEFAULT_LOCATION, lang: String) {
        viewModelScope.launch(Dispatchers.IO + excHandler) {
            weatherRepository.getWeather(location, lang).body()?.let {
                _weatherDataFlow.emit(it)
            }
        }
    }

}