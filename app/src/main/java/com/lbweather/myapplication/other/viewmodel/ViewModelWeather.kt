package com.lbweather.myapplication.other.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lbweather.myapplication.other.sharedPreferences.WeatherPref.weatherProperty
import com.lbweather.myapplication.domain.model.WeatherModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//class ViewModelWeather (
//    private val weatherRepository: WeatherRepository
//) : ViewModel() {
//
//    private val _weatherDataObject = MutableLiveData<WeatherModel?>(weatherProperty)
//    val weatherDataObject: LiveData<WeatherModel?> = _weatherDataObject
//
//    fun doRequestWeather(
//        location: String,
//        language: String
//    ) {
//
//        val coroutineHandler = CoroutineExceptionHandler { _, throwable ->
//            Log.i("myLog get Weather data exception", throwable.toString())
//            _weatherDataObject.postValue(null)
//        }
//
//        viewModelScope.launch(Dispatchers.IO + coroutineHandler) {
//            weatherRepository.getWeatherData(location, language).run {
//                _weatherDataObject.postValue(this.body())
//            }
//
//        }
//
//    }
//
//
//}