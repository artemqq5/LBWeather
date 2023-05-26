package com.lbweather.getweatherfromall.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lbweather.getweatherfromall.MyApp
import com.lbweather.getweatherfromall.domain.model.weather.WeatherModel
import com.lbweather.getweatherfromall.domain.repository.DefaultRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ViewModelWeather(
    private val repository: DefaultRepository,
) : ViewModel() {

    private val excHandler = CoroutineExceptionHandler { _, throwable ->
        MyApp.logData("Coroutine Exception. ViewModelWeather ($throwable)")
    }

    private val mutableFlowDataWeather = MutableSharedFlow<WeatherModel>(replay = 1)
    val flowDataWeather: SharedFlow<WeatherModel>
        get() = mutableFlowDataWeather.asSharedFlow()

    fun getWeatherData(location: String) {
        viewModelScope.launch(Dispatchers.IO + excHandler) {
            repository.getWeather(location).body()?.let {
                mutableFlowDataWeather.emit(it)
            }
        }
    }

    private val mutableFlowDataLocation = MutableSharedFlow<WeatherModel>()
    val flowDataLocation: SharedFlow<WeatherModel>
        get() = mutableFlowDataLocation.asSharedFlow()

    fun getLocationData(location: String) {
        viewModelScope.launch(Dispatchers.IO + excHandler) {
            repository.getWeather(location).body()?.let {
                mutableFlowDataLocation.emit(it)
            }
        }
    }


}