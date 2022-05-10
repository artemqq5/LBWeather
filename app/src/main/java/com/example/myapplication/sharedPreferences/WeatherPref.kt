package com.example.myapplication.sharedPreferences

import android.annotation.SuppressLint
import android.content.Context
import com.example.myapplication.MainActivity.Companion.main_context
import com.example.myapplication.weatherModelData.WeatherModel
import com.squareup.moshi.Moshi

object WeatherPref {

    private const val WEATHER_MODEL_KEY = "weather_model_preference"
    private val sharedWeather by lazy {
        main_context.getSharedPreferences(WEATHER_MODEL_KEY, Context.MODE_PRIVATE)
    }


    @SuppressLint("CommitPrefEdits")
    fun setShPrefWeather(weatherModel: WeatherModel) {
        sharedWeather.edit().putString(
            WEATHER_MODEL_KEY, weatherModel.toJson()
        ).apply()
    }

    fun getShPrefWeather(): WeatherModel? {
        return sharedWeather.getString(WEATHER_MODEL_KEY, null)?.toWeatherModel()
    }

    private fun WeatherModel.toJson(): String {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(WeatherModel::class.java)

        return adapter.toJson(this)
    }

    private fun String.toWeatherModel(): WeatherModel? {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(WeatherModel::class.java)

        return adapter.fromJson(this)
    }

}

