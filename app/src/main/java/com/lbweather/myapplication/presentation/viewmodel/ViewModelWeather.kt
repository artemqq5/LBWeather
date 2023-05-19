package com.lbweather.myapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lbweather.myapplication.domain.model.WeatherModel
import com.lbweather.myapplication.domain.repository.DefaultRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ViewModelWeather(
    private val repository: DefaultRepository,
) : ViewModel() {

    private val mutableFlowDataWeather = MutableSharedFlow<WeatherModel>(replay = 1)
    val flowDataWeather: SharedFlow<WeatherModel>
        get() = mutableFlowDataWeather.asSharedFlow()

    fun getWeatherData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getWeather().body()?.let {
                mutableFlowDataWeather.emit(it)
            }
        }
    }

}