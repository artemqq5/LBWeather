package com.lbweather.myapplication.sharedPreferences

import android.annotation.SuppressLint
import android.content.Context
import com.lbweather.myapplication.MainActivity.Companion.main_context
import com.lbweather.myapplication.location.LocationModel
import com.lbweather.myapplication.sharedPreferences.WeatherPref.toJson
import com.lbweather.myapplication.weatherModelData.WeatherModel
import com.squareup.moshi.Moshi

object WeatherPref {

    private const val WEATHER_MODEL_KEY = "weather_model_preference"
    private const val LOCATION_KEY = "location_preference"
    private val sharedWeather by lazy {
        main_context.getSharedPreferences(WEATHER_MODEL_KEY, Context.MODE_PRIVATE)
    }
    private val sharedLocation by lazy {
        main_context.getSharedPreferences(LOCATION_KEY, Context.MODE_PRIVATE)
    }

    var weatherProperty: WeatherModel?
        get() = sharedWeather.getString(WEATHER_MODEL_KEY, null)?.toWeatherModel()
        set(value) {
            sharedWeather.edit().putString(
                WEATHER_MODEL_KEY, value?.toJson()
            ).apply()
        }

    @SuppressLint("CommitPrefEdits")
    fun setShPrefLocation(locationModel: LocationModel) {
        sharedLocation.edit().putString(
            LOCATION_KEY, locationModel.toJson()
        ).apply()
    }

    fun getShPrefLocation(): LocationModel? {
        return sharedLocation.getString(LOCATION_KEY, null)?.toLocationModel()
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

    private fun LocationModel.toJson(): String {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(LocationModel::class.java)

        return adapter.toJson(this)
    }

    private fun String.toLocationModel(): LocationModel? {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(LocationModel::class.java)

        return adapter.fromJson(this)
    }


}

