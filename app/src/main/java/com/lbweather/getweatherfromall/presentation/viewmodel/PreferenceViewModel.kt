package com.lbweather.getweatherfromall.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lbweather.getweatherfromall.MyApp.Companion.logData
import com.lbweather.getweatherfromall.domain.repository.PreferenceRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class PreferenceViewModel(private val preferenceRepository: PreferenceRepository) : ViewModel() {

    private val excHandler = CoroutineExceptionHandler { _, throwable ->
        logData("Coroutine Exception. PreferenceViewModel ($throwable)")
    }

    private val dataTimeFormat = preferenceRepository.getTimeFormat
    private val dataUnitTemperature = preferenceRepository.getUnitTemperature

    val unitOfMeasurement = dataTimeFormat.combine(dataUnitTemperature) { timeFormat, unitTemperature ->
        Pair(timeFormat, unitTemperature)
    }

    fun setTimeFormat(timeFormat: String) {
        viewModelScope.launch(excHandler) {
            logData(timeFormat)
            preferenceRepository.setTimeFormat(timeFormat)
        }
    }

    fun setUnitTemperature(unitTemperature: String) {
        viewModelScope.launch(excHandler) {
            logData(unitTemperature)
            preferenceRepository.setUnitTemperature(unitTemperature)
        }
    }


}