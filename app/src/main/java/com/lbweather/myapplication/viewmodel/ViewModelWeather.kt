package com.lbweather.myapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lbweather.myapplication.network.requestAPI.WeatherRepository
import com.lbweather.myapplication.sharedPreferences.WeatherPref
import com.lbweather.myapplication.weatherModelData.WeatherModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModelWeather : ViewModel() {

    private val weatherRepository by lazy {
        WeatherRepository()
    }

    val weatherDataObject by lazy {
        MutableLiveData<WeatherModel>()
    }

    fun doRequestWeather(
        location: String,
        language: String,
        func: (Boolean) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            getDataFromServer(location, language, func)
        }

    }

    // main request to get WeatherModel object
    private suspend fun getDataFromServer(
        location: String,
        language: String,
        func: (Boolean) -> Unit
    ) {
        var result = true

        val job = viewModelScope.launch(Dispatchers.IO) {
            try {
                weatherRepository.getWeatherData(location, language).run {
                    if (this.isSuccessful) {
                        weatherDataObject.postValue(this.body())
                        WeatherPref.setShPrefWeather(this.body()!!)
                    } else {
                        result = false
                    }
                }

            } catch (e: Exception) {
                result = false
            }

        }

        job.join()
        withContext(Dispatchers.Main) {
            func(result)
        }

    }


}