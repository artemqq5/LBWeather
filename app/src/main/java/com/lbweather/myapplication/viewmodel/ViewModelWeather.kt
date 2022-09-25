package com.lbweather.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lbweather.myapplication.network.requestAPI.WeatherRepository
import com.lbweather.myapplication.sharedPreferences.WeatherPref.weatherProperty
import com.lbweather.myapplication.weatherModelData.WeatherModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ViewModelWeather @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _weatherDataObject = MutableLiveData<WeatherModel?>(weatherProperty)
    val weatherDataObject: LiveData<WeatherModel?> = _weatherDataObject

    fun doRequestWeather(
        location: String,
        language: String,
        func: (Boolean) -> Unit
    ) {

        var result = true

        val coroutineHandler = CoroutineExceptionHandler { _, throwable ->
            Log.i("myLog get Weather data exception", throwable.toString())
        }

        viewModelScope.launch(Dispatchers.IO + coroutineHandler) {
            weatherRepository.getWeatherData(location, language).run {
                if (this.isSuccessful) {
                    _weatherDataObject.postValue(this.body())
                    weatherProperty = this.body()
                } else {
                    result = false
                }

                withContext(Dispatchers.Main) {
                    func(result)
                }
            }

        }

    }


}