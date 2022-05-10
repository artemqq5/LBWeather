package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.network.WeatherRepository
import com.example.myapplication.sharedPreferences.WeatherPref
import com.example.myapplication.weatherModelData.WeatherModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModelWeather : ViewModel() {

    private val weatherRepository by lazy {
        WeatherRepository()
    }

    val weatherDataObject by lazy {
        MutableLiveData<WeatherModel>()
    }

    fun gg(
        location: String,
        language: String,
        func: (Int) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            getDataFromServer(location, language, func)
        }

    }

    // main request to get WeatherModel object
    private suspend fun getDataFromServer(
        location: String,
        language: String,
        func: (Int) -> Unit
    ) {
        var result = 1

        Log.i("tytgyhu3j2", "request to API Weather")
        val job = viewModelScope.launch(Dispatchers.IO) {
            try {
                weatherRepository.getWeatherData(location, language).body()?.let {
                    weatherDataObject.postValue(it)
                    WeatherPref.setShPrefWeather(it)
                }

            } catch (e: Exception) {
                Log.i("tytgyhu3j2", " отримання інфо ${e.message}")
                result = -1
            }

        }

        job.join()
        withContext(Dispatchers.Main) {
            try {
                func(result)
            } catch (e: Exception) {
                Log.i("tytgyhu3j2", " перехід  ${e.message}")
            }

        }

    }


}