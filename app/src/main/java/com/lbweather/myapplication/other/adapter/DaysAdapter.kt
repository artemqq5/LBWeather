package com.lbweather.myapplication.other.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lbweather.myapplication.R
import com.lbweather.myapplication.databinding.ItemDayRecyclerBinding
import com.lbweather.myapplication.other.fragments.settings_fragments.PreferencesObject
import com.lbweather.myapplication.other.helper.FromStr.fromStr
import com.lbweather.myapplication.other.helper.GlideLoader.loadImg
import com.lbweather.myapplication.other.helper.StateUnit
import com.lbweather.myapplication.other.helper.StateUnit.isCelsius
import com.lbweather.myapplication.other.helper.TimeFormat
import com.lbweather.myapplication.other.helper.TimeFormat.getParsingTime
import com.lbweather.myapplication.domain.model.Forecastday

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



            binding.temperatureText.text = if (itemView.context.isCelsius()) {
                "${itemView.context.fromStr(R.string.temperature)}: ${modelData.mintemp_c}/${modelData.maxtemp_c} ${
                    itemView.context.fromStr(
                        R.string.celsius
                    )
                }"
            } else "${itemView.context.fromStr(R.string.temperature)}: ${modelData.mintemp_f}/${modelData.maxtemp_f} ${
                itemView.context.fromStr(
                    R.string.fahrenheit
                )
            }"

            binding.windSpeedText.text = if (StateUnit.isKilometer()) {
                "${itemView.context.fromStr(R.string.speedWind)}: ${modelData.maxwind_kph} ${
                    itemView.context.fromStr(
                        R.string.kph
                    )
                }"
            } else "${itemView.context.fromStr(R.string.speedWind)}: ${modelData.maxwind_mph} ${
                itemView.context.fromStr(
                    R.string.mph
                )
            }"

            binding.sunriseText.text =
                "${itemView.context.fromStr(R.string.sunrise)}: ${
                    if (StateUnit.isTime24()) {
                        modelDataAstro.sunrise.getParsingTime(
                            TimeFormat.HOUR_MINUTE_AA,
                            TimeFormat.HOUR_MINUTE
                        )
                    } else modelDataAstro.sunrise

                }"
            binding.sunset.text =
                "${itemView.context.fromStr(R.string.sunset)}: ${
                    if (StateUnit.isTime24()) {
                        modelDataAstro.sunset.getParsingTime(
                            TimeFormat.HOUR_MINUTE_AA,
                            TimeFormat.HOUR_MINUTE
                        )
                    } else modelDataAstro.sunset

                }"

            binding.humidityText.text =
                "${itemView.context.fromStr(R.string.humidity)}: ${modelData.avghumidity} %"
            binding.infoAboutText.text = modelData.condition.text
            binding.infoAboutImage.loadImg(modelData.condition.icon)

            // hide some elements if sharedPreferences false
            if (PreferencesObject.getValuePreference(PreferencesObject.CHANCE_RAIN_DAY)) {
                binding.chanceRainText.text =
                    "${itemView.context.fromStr(R.string.rainChance)}: ${modelData.daily_chance_of_rain} %"
            } else binding.chanceRainText.visibility = View.GONE

            if (PreferencesObject.getValuePreference(PreferencesObject.CHANCE_SNOW_DAY)) {
                binding.chanceSnowText.text =
                    "${itemView.context.fromStr(R.string.snowChance)}: ${modelData.daily_chance_of_snow} %"
            } else binding.chanceSnowText.visibility = View.GONE

            if (PreferencesObject.getValuePreference(PreferencesObject.ULTRAVIOLET_DAY)) {
                binding.uvIndexText.text =
                    "${itemView.context.fromStr(R.string.uv)}: ${modelData.uv}"
            } else binding.uvIndexText.visibility = View.GONE


        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_day_recycler, parent, false)

        return ViewHolder(view)
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