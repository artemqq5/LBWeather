package com.example.myapplication.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentDetailsHourBinding
import com.example.myapplication.fragments.settings_fragments.PreferencesObject.CHANCE_RAIN_HOUR
import com.example.myapplication.fragments.settings_fragments.PreferencesObject.CHANCE_SNOW_HOUR
import com.example.myapplication.fragments.settings_fragments.PreferencesObject.FEEL_TEMP_HOUR
import com.example.myapplication.fragments.settings_fragments.PreferencesObject.ULTRAVIOLET_HOUR
import com.example.myapplication.fragments.settings_fragments.PreferencesObject.getValuePreference
import com.example.myapplication.helper.FromStr
import com.example.myapplication.helper.FromStr.fromStr
import com.example.myapplication.helper.GlideLoader.loadImg
import com.example.myapplication.helper.StateUnit
import com.example.myapplication.helper.StateUnit.isCelsius
import com.example.myapplication.helper.StateUnit.isKilometer
import com.example.myapplication.helper.StateUnit.isTime24
import com.example.myapplication.helper.TimeFormat.DAYWEEK_DAY_MONTH_YEAR
import com.example.myapplication.helper.TimeFormat.HOUR_MINUTE
import com.example.myapplication.helper.TimeFormat.HOUR_MINUTE_AA
import com.example.myapplication.helper.TimeFormat.YEAR_MONTH_DAY_HOUR_MINUTE
import com.example.myapplication.helper.TimeFormat.getParsingTime
import com.example.myapplication.weatherModelData.Hour

class DetailsDataHour : Fragment() {

    private lateinit var binding: FragmentDetailsHourBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsHourBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dayNumber = requireArguments().getParcelable<Hour>("timeHour")
        setWeatherData(dayNumber!!)

    }

    @SuppressLint("SetTextI18n")
    private fun setWeatherData(timeHour: Hour) {

        val modelDataDate = timeHour.time

        binding.dataText.text =
            modelDataDate.getParsingTime(YEAR_MONTH_DAY_HOUR_MINUTE, DAYWEEK_DAY_MONTH_YEAR)
        binding.timeText.text = if (isTime24()) {
            modelDataDate.getParsingTime(YEAR_MONTH_DAY_HOUR_MINUTE, HOUR_MINUTE)
        } else modelDataDate.getParsingTime(YEAR_MONTH_DAY_HOUR_MINUTE, HOUR_MINUTE_AA)


        binding.temperatureText.text = if (isCelsius()) {
            "${fromStr(R.string.temperature)}: ${timeHour.temp_c} ${fromStr(R.string.celsius)}"
        } else "${fromStr(R.string.temperature)}: ${timeHour.temp_f} ${fromStr(R.string.fahrenheit)}"

        binding.windSpeedText.text = if (isKilometer()) {
            "${fromStr(R.string.speedWind)}: ${timeHour.wind_kph} ${fromStr(R.string.kph)}"
        } else "${fromStr(R.string.speedWind)}: ${timeHour.wind_mph} ${fromStr(R.string.mph)}"

        binding.humidityText.text = "${fromStr(R.string.humidity)}: ${timeHour.humidity} %"
        binding.infoWeatherText.text = timeHour.condition.text
        binding.infoWeatherIcon.loadImg(timeHour.condition.icon)

        // hide some elements if they are nul or sharedPreferences false
        if (timeHour.chance_of_rain == null || timeHour.chance_of_snow == null) {
            binding.chanceRainText.visibility = View.GONE
            binding.chanceSnowText.visibility = View.GONE
        } else {
            if (getValuePreference(CHANCE_RAIN_HOUR)) {
                binding.chanceRainText.text =
                    "${fromStr(R.string.rainChance)}: ${timeHour.chance_of_rain} %"
            } else binding.chanceRainText.visibility = View.GONE

            if (getValuePreference(CHANCE_SNOW_HOUR)) {
                binding.chanceSnowText.text =
                    "${fromStr(R.string.snowChance)}: ${timeHour.chance_of_snow} %"
            } else binding.chanceSnowText.visibility = View.GONE
        }

        if (getValuePreference(ULTRAVIOLET_HOUR)) {
            binding.uvIndexText.text = "${fromStr(R.string.uv)}: ${timeHour.uv}"
        } else binding.uvIndexText.visibility = View.GONE

        if (getValuePreference(FEEL_TEMP_HOUR)) {
            binding.feelTempText.text = if (isCelsius()) {
                "${fromStr(R.string.tempFeels)}: ${timeHour.feelslike_c} ${fromStr(R.string.celsius)}"
            } else "${fromStr(R.string.tempFeels)}: ${timeHour.feelslike_f} ${fromStr(R.string.fahrenheit)}"

        } else binding.feelTempText.visibility = View.GONE


    }
}