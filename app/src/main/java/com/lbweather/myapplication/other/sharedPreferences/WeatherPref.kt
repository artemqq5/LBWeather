package com.lbweather.myapplication.other.sharedPreferences
//
//import android.annotation.SuppressLint
//import android.content.Context
//import com.lbweather.myapplication.MainActivity.Companion.main_context
//import com.lbweather.myapplication.domain.model.weather.WeatherModel
//import com.squareup.moshi.Moshi
//
//object WeatherPref {
//
//    private const val WEATHER_MODEL_KEY = "weather_model_preference"
//    private const val LOCATION_KEY = "location_preference"
//    private const val LOCATION_LIST_KEY = "location_list_preference"
//    private val mainPreferences by lazy {
//        main_context.getSharedPreferences("my-preferences", Context.MODE_PRIVATE)
//    }
//
//    var weatherProperty: WeatherModel?
//        get() = mainPreferences.getString(WEATHER_MODEL_KEY, null)?.toWeatherModel()
//        set(value) {
//            mainPreferences.edit().putString(
//                WEATHER_MODEL_KEY, value?.toJson()
//            ).apply()
//        }
//
//    @SuppressLint("CommitPrefEdits")
//    fun setShPrefLocation(locationModel: LocationModel) {
//        mainPreferences.edit().putString(
//            LOCATION_KEY, locationModel.toJson()
//        ).apply()
//    }
//
//    fun getShPrefLocation(): LocationModel? {
//        return mainPreferences.getString(LOCATION_KEY, null)?.toLocationModel()
//    }
//
//    fun setListLocationsPreference(value: List<LocationModel>) {
//        val setListLocation = mutableSetOf<String>()
//        for(i in value) {
//            setListLocation.add(
//                i.toJson()
//            )
//        }
//
//        mainPreferences.edit().putStringSet(LOCATION_LIST_KEY, setListLocation).apply()
//    }
//
//    fun getListLocationsPreference(): List<LocationModel> {
//        val listLocation = mutableListOf<LocationModel>()
//        mainPreferences.getStringSet(LOCATION_LIST_KEY, null)?.let {
//            for (i in it) {
//                listLocation.add(i.toLocationModel())
//            }
//        }
//
//        return listLocation
//    }
//
//    private fun WeatherModel.toJson(): String {
//        val moshi = Moshi.Builder().build()
//        val adapter = moshi.adapter(WeatherModel::class.java)
//
//        return adapter.toJson(this)
//    }
//
//    private fun String.toWeatherModel(): WeatherModel? {
//        val moshi = Moshi.Builder().build()
//        val adapter = moshi.adapter(WeatherModel::class.java)
//
//        return adapter.fromJson(this)
//    }
//
//    private fun LocationModel.toJson(): String {
//        val moshi = Moshi.Builder().build()
//        val adapter = moshi.adapter(LocationModel::class.java)
//
//        return adapter.toJson(this)
//    }
//
//    private fun String.toLocationModel(): LocationModel {
//        val moshi = Moshi.Builder().build()
//        val adapter = moshi.adapter(LocationModel::class.java)
//
//        return adapter.fromJson(this)!!
//    }
//
//
//}
//
