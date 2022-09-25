package com.lbweather.myapplication.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lbweather.myapplication.R
import com.lbweather.myapplication.databinding.ItemDayRecyclerBinding
import com.lbweather.myapplication.fragments.settings_fragments.PreferencesObject
import com.lbweather.myapplication.helper.FromStr
import com.lbweather.myapplication.helper.GlideLoader.loadImg
import com.lbweather.myapplication.helper.StateUnit
import com.lbweather.myapplication.helper.TimeFormat
import com.lbweather.myapplication.helper.TimeFormat.getParsingTime
import com.lbweather.myapplication.weatherModelData.Forecastday
import com.lbweather.myapplication.weatherModelData.Hour
import com.lbweather.myapplication.weatherModelData.WeatherModel

class DaysAdapter(private var dataSet: Array<Forecastday>) : RecyclerView.Adapter<DaysAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemDayRecyclerBinding.bind(view)

        @SuppressLint("SetTextI18n")
        fun setWeatherData(weatherModel: Forecastday) {
            val modelData = weatherModel.day
            val modelDataDate = weatherModel.date
            val modelDataAstro = weatherModel.astro

            binding.dataText.text =
                modelDataDate.getParsingTime(
                    TimeFormat.YEAR_MONTH_DAY,
                    TimeFormat.DAYWEEK_DAY_MONTH_YEAR
                )

            binding.temperatureText.text = if (StateUnit.isCelsius()) {
                "${FromStr.fromStr(R.string.temperature)}: ${modelData.mintemp_c}/${modelData.maxtemp_c} ${
                    FromStr.fromStr(
                        R.string.celsius
                    )
                }"
            } else "${FromStr.fromStr(R.string.temperature)}: ${modelData.mintemp_f}/${modelData.maxtemp_f} ${
                FromStr.fromStr(
                    R.string.fahrenheit
                )
            }"

            binding.windSpeedText.text = if (StateUnit.isKilometer()) {
                "${FromStr.fromStr(R.string.speedWind)}: ${modelData.maxwind_kph} ${
                    FromStr.fromStr(
                        R.string.kph
                    )
                }"
            } else "${FromStr.fromStr(R.string.speedWind)}: ${modelData.maxwind_mph} ${
                FromStr.fromStr(
                    R.string.mph
                )
            }"

            binding.sunriseText.text =
                "${FromStr.fromStr(R.string.sunrise)}: ${
                    if (StateUnit.isTime24()) {
                        modelDataAstro.sunrise.getParsingTime(
                            TimeFormat.HOUR_MINUTE_AA,
                            TimeFormat.HOUR_MINUTE
                        )
                    } else modelDataAstro.sunrise

                }"
            binding.sunset.text =
                "${FromStr.fromStr(R.string.sunset)}: ${
                    if (StateUnit.isTime24()) {
                        modelDataAstro.sunset.getParsingTime(
                            TimeFormat.HOUR_MINUTE_AA,
                            TimeFormat.HOUR_MINUTE
                        )
                    } else modelDataAstro.sunset

                }"

            binding.humidityText.text = "${FromStr.fromStr(R.string.humidity)}: ${modelData.avghumidity} %"
            binding.infoAboutText.text = modelData.condition.text
            binding.infoAboutImage.loadImg(modelData.condition.icon)

            // hide some elements if sharedPreferences false
            if (PreferencesObject.getValuePreference(PreferencesObject.CHANCE_RAIN_DAY)) {
                binding.chanceRainText.text =
                    "${FromStr.fromStr(R.string.rainChance)}: ${modelData.daily_chance_of_rain} %"
            } else binding.chanceRainText.visibility = View.GONE

            if (PreferencesObject.getValuePreference(PreferencesObject.CHANCE_SNOW_DAY)) {
                binding.chanceSnowText.text =
                    "${FromStr.fromStr(R.string.snowChance)}: ${modelData.daily_chance_of_snow} %"
            } else binding.chanceSnowText.visibility = View.GONE

            if (PreferencesObject.getValuePreference(PreferencesObject.ULTRAVIOLET_DAY)) {
                binding.uvIndexText.text = "${FromStr.fromStr(R.string.uv)}: ${modelData.uv}"
            } else binding.uvIndexText.visibility = View.GONE


        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_day_recycler, parent, false)

        return DaysAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setWeatherData(dataSet[position])
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(newList: Array<Forecastday>) {
        dataSet = newList
        notifyDataSetChanged()
    }


}