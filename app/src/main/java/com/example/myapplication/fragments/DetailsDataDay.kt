package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.myapplication.databinding.FragmentDetailsDayBinding
import com.example.myapplication.fragments.settings_fragments.PreferencesObject.CHANCE_RAIN_DAY
import com.example.myapplication.fragments.settings_fragments.PreferencesObject.CHANCE_SNOW_DAY
import com.example.myapplication.fragments.settings_fragments.PreferencesObject.ULTRAVIOLET_DAY
import com.example.myapplication.fragments.settings_fragments.PreferencesObject.getValuePreference
import com.example.myapplication.helper.GlideLoader.loadImg
import com.example.myapplication.helper.TimeFormat.DAYWEEK_DAY_MONTH_YEAR
import com.example.myapplication.helper.TimeFormat.HOUR_MINUTE
import com.example.myapplication.helper.TimeFormat.HOUR_MINUTE_AA
import com.example.myapplication.helper.TimeFormat.YEAR_MONTH_DAY
import com.example.myapplication.helper.TimeFormat.getParsingTime
import com.example.myapplication.viewmodel.ViewModelWeather
import com.example.myapplication.weatherModelData.WeatherModel

class DetailsDataDay : Fragment() {

    private lateinit var binding: FragmentDetailsDayBinding
    private val viewModelWeather: ViewModelWeather by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsDayBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelWeather.weatherDataObject.observe(viewLifecycleOwner) { weatherModel ->
            weatherModel?.let {
                val dayNumber = requireArguments().getInt("day")

                // method to set data to display
                it.setWeatherData(dayNumber)

            }
        }

    }

    private fun WeatherModel.setWeatherData(day: Int) {
        val modelData = this.forecast.forecastday[day].day
        val modelDataDate = this.forecast.forecastday[day].date
        val modelDataAstro = this.forecast.forecastday[day].astro

        binding.dataText.text =
            modelDataDate.getParsingTime(YEAR_MONTH_DAY, DAYWEEK_DAY_MONTH_YEAR)

        binding.temperatureText.text =
            "Темпепетура: ${modelData.mintemp_c}/${modelData.maxtemp_c} °С"
        binding.humidityText.text = "Вологість: ${modelData.avghumidity} %"
        binding.windSpeedText.text = "Швидкість вітру: ${modelData.maxwind_kph} км/год"
        binding.sunriseText.text =
            "Схід сонця: ${modelDataAstro.sunrise.getParsingTime(HOUR_MINUTE_AA, HOUR_MINUTE)}"
        binding.sunset.text =
            "Захід сонця: ${modelDataAstro.sunset.getParsingTime(HOUR_MINUTE_AA, HOUR_MINUTE)}"
        binding.infoAboutText.text = modelData.condition.text
        binding.infoAboutImage.loadImg(modelData.condition.icon)

        // hide some elements if sharedPreferences false
        if (getValuePreference(CHANCE_RAIN_DAY)) {
            binding.chanceRainText.text = "Ймовірність дощу: ${modelData.daily_chance_of_rain} %"
        } else binding.chanceRainText.visibility = View.GONE

        if (getValuePreference(CHANCE_SNOW_DAY)) {
            binding.chanceSnowText.text = "Ймовірність снігу: ${modelData.daily_chance_of_snow} %"
        } else binding.chanceSnowText.visibility = View.GONE

        if (getValuePreference(ULTRAVIOLET_DAY)) {
            binding.uvIndexText.text = "Ультрафіолет: ${modelData.uv}"
        } else binding.uvIndexText.visibility = View.GONE


    }
}