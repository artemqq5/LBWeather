package com.lbweather.getweatherfromall.presentation.fragments

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.lbweather.getweatherfromall.MyApp.Companion.logData
import com.lbweather.getweatherfromall.R
import com.lbweather.getweatherfromall.databinding.FragmentFutureForecastBinding
import com.lbweather.getweatherfromall.domain.model.HourValueModel
import com.lbweather.getweatherfromall.domain.model.weather.HourModel
import com.lbweather.getweatherfromall.domain.model.weather.WeatherDataModel
import com.lbweather.getweatherfromall.domain.usecase.DateTimeUseCase
import com.lbweather.getweatherfromall.domain.usecase.GoogleAdsUseCase
import com.lbweather.getweatherfromall.domain.usecase.GoogleAdsUseCase.Companion.ID_FUTURE_WEATHER_BANNER
import com.lbweather.getweatherfromall.presentation.adapters.CustomAdapter
import com.lbweather.getweatherfromall.presentation.viewmodel.LocationViewModel
import com.lbweather.getweatherfromall.presentation.viewmodel.PreferenceViewModel
import com.lbweather.getweatherfromall.presentation.viewmodel.WeatherViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.util.Locale


class FutureForecastFragment : Fragment() {

    private lateinit var binding: FragmentFutureForecastBinding

    private val weatherViewModel: WeatherViewModel by activityViewModel()
    private val locationViewModel: LocationViewModel by activityViewModel()
    private val preferenceViewModel: PreferenceViewModel by activityViewModel()

    private val dateTimeUseCase: DateTimeUseCase by inject()

    private val adapterWeatherToday by lazy {
        CustomAdapter()
    }

    private val adapterWeatherTomorrow by lazy {
        CustomAdapter()
    }

    private val adapterWeatherAfterDayTomorrow by lazy {
        CustomAdapter()
    }

    private val excHandler = CoroutineExceptionHandler { _, throwable ->
        logData("Coroutine Exception. FutureForecastFragment ($throwable)")
    }

    private var adView: AdView? = null
    private val adSize: AdSize
        get() {
            val bounds: Rect = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                requireActivity().windowManager.currentWindowMetrics.bounds
            } else {
                val displayMetrics = DisplayMetrics()
                @Suppress("DEPRECATION")
                requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
                Rect(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
            }

            var adWidthPixels = binding.adsBannerBox.width.toFloat()

            if (adWidthPixels == 0f) {
                adWidthPixels = bounds.width().toFloat()
            }

            val density = resources.displayMetrics.density
            val adWidth = (adWidthPixels / density).toInt()

            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                requireContext(),
                adWidth
            )
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFutureForecastBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adView = AdView(requireContext())
        binding.adsBannerBox.addView(adView)

        binding.adsBannerBox.viewTreeObserver.addOnGlobalLayoutListener {
            if (adView?.adUnitId == null || adView?.adSize == null) {
                loadBanner()
            }
        }

        binding.todayList.adapter = adapterWeatherToday
        binding.tomorrowList.adapter = adapterWeatherTomorrow
        binding.afterTomorrowList.adapter = adapterWeatherAfterDayTomorrow

        lifecycleScope.launch(excHandler) {
            weatherViewModel.weatherDataFlow.collectLatest {
                // disable updating swipe progress bar
                binding.swipeLayoutFuture.isRefreshing = false

                setUIDataWeather(it)
            }
        }

        binding.swipeLayoutFuture.setOnRefreshListener {
            lifecycleScope.launch(Dispatchers.IO + excHandler) {
                val currentLocation = locationViewModel.getActiveLocation()
                weatherViewModel.updateWeather(
                    location = currentLocation.shortLocation,
                    lang = Locale.getDefault().language
                )
            }
        }

    }

    private fun setUIDataWeather(weatherData: WeatherDataModel) {
        lifecycleScope.launch(Dispatchers.Main + excHandler) {
            preferenceViewModel.unitOfMeasurement.collectLatest { unitOfMeasurement ->
                val timeFormat = unitOfMeasurement.first
                val temperatureFormat = unitOfMeasurement.second

                // set new data list for hours weather today
                adapterWeatherToday.setList(weatherData.forecastWeatherModel.forecastDayModel[0].hourModel.map { hour ->
                    HourValueModel(
                        conditionModel = hour.conditionModel,
                        temp = hour.toMeasureDataTemperature(temperatureFormat),
                        time = hour.time.toMeasureDataTime(timeFormat),
                    )
                })

                // set new data list for hours weather tomorrow
                adapterWeatherTomorrow.setList(weatherData.forecastWeatherModel.forecastDayModel[1].hourModel.map { hour ->
                    HourValueModel(
                        conditionModel = hour.conditionModel,
                        temp = hour.toMeasureDataTemperature(temperatureFormat),
                        time = hour.time.toMeasureDataTime(timeFormat),
                    )
                })

                // set new data list for hours weather after day tomorrow
                adapterWeatherAfterDayTomorrow.setList(weatherData.forecastWeatherModel.forecastDayModel[2].hourModel.map { hour ->
                    HourValueModel(
                        conditionModel = hour.conditionModel,
                        temp = hour.toMeasureDataTemperature(temperatureFormat),
                        time = hour.time.toMeasureDataTime(timeFormat),
                    )
                })

            }
        }
    }

    private fun Long.toMeasureDataTime(format: String): String {
        val date = dateTimeUseCase.timeEpochToDate(this)

        return when (format) {
            resources.getStringArray(R.array.list_times_value)[0] -> {
                // by default
                dateTimeUseCase.getFormatTimeForLocale(date)
            }

            resources.getStringArray(R.array.list_times_value)[1] -> {
                // europe
                dateTimeUseCase.getFormatTime24(date)
            }

            else -> {
                dateTimeUseCase.getFormatTime12(date)
            }
        }
    }

    private fun HourModel.toMeasureDataTemperature(format: String): Double {
        return when (format) {
            resources.getStringArray(R.array.list_temperature_value)[0] -> {
                // by celsius
                this.tempC
            }

            else -> {
                this.tempF
            }
        }
    }

    private fun loadBanner() {
        adView?.let { adView ->
            adView.adUnitId = ID_FUTURE_WEATHER_BANNER
            adView.setAdSize(adSize)
            adView.loadAd(AdRequest.Builder().build())
        }

    }
}