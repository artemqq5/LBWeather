package com.lbweather.getweatherfromall.presentation.fragments

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.lbweather.getweatherfromall.MyApp.Companion.logData
import com.lbweather.getweatherfromall.R
import com.lbweather.getweatherfromall.databinding.FragmentDisplayWeatherBinding
import com.lbweather.getweatherfromall.domain.model.HourValueModel
import com.lbweather.getweatherfromall.domain.model.LocationUserModel
import com.lbweather.getweatherfromall.domain.model.weather.HourModel
import com.lbweather.getweatherfromall.domain.model.weather.WeatherDataModel
import com.lbweather.getweatherfromall.domain.usecase.ConnectionManagerUseCase
import com.lbweather.getweatherfromall.domain.usecase.DateTimeUseCase
import com.lbweather.getweatherfromall.domain.usecase.GoogleAdsUseCase.Companion.ID_BOTTOM_SHEET_BANNER
import com.lbweather.getweatherfromall.presentation.adapters.CustomAdapter
import com.lbweather.getweatherfromall.presentation.adapters.LocationAdapter
import com.lbweather.getweatherfromall.presentation.adapters.NavigationCustomAdapter
import com.lbweather.getweatherfromall.presentation.viewmodel.LocationViewModel
import com.lbweather.getweatherfromall.presentation.viewmodel.PreferenceViewModel
import com.lbweather.getweatherfromall.presentation.viewmodel.WeatherViewModel
import com.lbweather.getweatherfromall.presentation.viewmodel.WeatherViewModel.Companion.DEFAULT_LOCATION
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.util.*


class DisplayWeather : Fragment(), NavigationCustomAdapter {

    private lateinit var binding: FragmentDisplayWeatherBinding

    private val adapterWeather by lazy {
        CustomAdapter()
    }

    private val locationAdapter by lazy {
        LocationAdapter(listenerClick = this)
    }

    private val internetSnackBar by lazy {
        Snackbar.make(
            binding.root, resources.getString(R.string.no_internet), Snackbar.LENGTH_INDEFINITE
        )
    }

    private val dateTimeUseCase: DateTimeUseCase by inject()
    private val connectionManagerUseCase: ConnectionManagerUseCase by inject()

    private val weatherViewModel: WeatherViewModel by activityViewModel()
    private val locationViewModel: LocationViewModel by activityViewModel()
    private val preferenceViewModel: PreferenceViewModel by activityViewModel()

    private val excHandler = CoroutineExceptionHandler { _, throwable ->
        logData("Coroutine Exception. DisplayWeather ($throwable)")
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

            var adWidthPixels = binding.bottomSheet.adsBannerBox.width.toFloat()

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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDisplayWeatherBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adView = AdView(requireContext())
        binding.bottomSheet.adsBannerBox.addView(adView)

        binding.bottomSheet.adsBannerBox.viewTreeObserver.addOnGlobalLayoutListener {
            if (adView?.adUnitId == null || adView?.adSize == null) {
                loadBanner()
            }
        }

        // bind locations list adapter to recyclerView
        binding.bottomSheet.recyclerLocation.adapter = locationAdapter

        // bind adapter to recyclerView
        binding.bottomSheet.recyclerView.adapter = adapterWeather

        binding.bottomSheet.recyclerView.viewTreeObserver.addOnGlobalLayoutListener {
            BottomSheetBehavior.from(binding.bottomSheet.root).apply {
                peekHeight = getMinHeightBottomSheet() // calculate min height
                state = BottomSheetBehavior.STATE_COLLAPSED
            }

            lifecycleScope.launch {
                delay(10L)
                binding.bottomSheet.root.visibility = View.VISIBLE
            }
        }

        lifecycleScope.launch(excHandler) {
            weatherViewModel.weatherDataFlow.collectLatest { weatherData ->
                // disable updating swipe progress bar
                binding.swipeLayout.isRefreshing = false

                // set data to current weather
                setUIDataWeather(weatherData)
            }
        }

        lifecycleScope.launch(excHandler) {
            locationViewModel.allLocationData.collectLatest { locationsUser ->
                // update list locations
                locationAdapter.setList(locationsUser)

                if (!connectionManagerUseCase.isInternet()) {
                    internetSnackBar.show()
                }

                lifecycleScope.launch(excHandler) {
                    connectionManagerUseCase.changeStateConnection().collectLatest {
                        if (it == ConnectionManagerUseCase.StatusInternet.Available) {
                            internetSnackBar.dismiss()
                            // update weather
                            weatherViewModel.updateWeather(
                                location = locationsUser.find { location ->
                                    location.status
                                }?.shortLocation ?: DEFAULT_LOCATION,
                                lang = Locale.getDefault().language
                            )
                        } else {
                            internetSnackBar.show()
                        }
                    }
                }

            }
        }


        // add swipe listener to delete item
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (locationAdapter.locationList.size > 1) {
                    // get current active location
                    val model = locationAdapter.locationList[viewHolder.adapterPosition]
                    // update state to active for first other location
                    if (model.status) locationViewModel.updateActiveLocation(locationAdapter.locationList.first { it != model })
                    // remove last current location
                    locationViewModel.deleteLocation(model)
                }
            }

        }).attachToRecyclerView(binding.bottomSheet.recyclerLocation)


        // create listener on bottom sheet state change
        BottomSheetBehavior.from(binding.bottomSheet.root)
            .addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        binding.bottomSheet.recyclerView.scrollToPosition(0)
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    binding.bottomSheet.menuLocations.alpha = 1 - slideOffset
                    binding.bottomSheet.menuLocations.isEnabled = (1 - slideOffset >= 1)
                }

            })

        // update data by swipe
        binding.swipeLayout.setOnRefreshListener {
            lifecycleScope.launch(Dispatchers.IO + excHandler) {
                val currentLocation = locationViewModel.getActiveLocation()
                weatherViewModel.updateWeather(
                    location = currentLocation.shortLocation, lang = Locale.getDefault().language
                )
            }
        }


        // open fragment with search location
        binding.bottomSheet.addLocation.setOnClickListener {
            findNavController().navigate(R.id.action_displayWeather_to_dialogListLocations)
        }

        binding.bottomSheet.openSettings.setOnClickListener {
            findNavController().navigate(R.id.action_parentDisplayFragment_to_settingsFragment)
        }

        // open bottom sheet with saved locations
        binding.bottomSheet.menuLocations.setOnClickListener {
            BottomSheetBehavior.from(binding.bottomSheet.root).state =
                BottomSheetBehavior.STATE_EXPANDED
        }

    }

    private fun setUIDataWeather(weatherData: WeatherDataModel) {
        lifecycleScope.launch(excHandler) {
            preferenceViewModel.unitOfMeasurement.collectLatest { unitOfMeasurement ->
                val timeFormat = unitOfMeasurement.first
                val temperatureFormat = unitOfMeasurement.second

                if (temperatureFormat == resources.getStringArray(R.array.list_temperature_value)[0]) {
                    // celsius
                    binding.locationTemperature.text =
                        weatherData.currentWeatherModel.tempC.toString()
                    binding.lowTemperature.text =
                        weatherData.forecastWeatherModel.forecastDayModel[0].dayModel.minTempCParsed
                    binding.highTemperature.text =
                        weatherData.forecastWeatherModel.forecastDayModel[0].dayModel.maxTempCParsed
                } else {
                    binding.locationTemperature.text =
                        weatherData.currentWeatherModel.tempF.toString()
                    binding.lowTemperature.text =
                        weatherData.forecastWeatherModel.forecastDayModel[0].dayModel.minTempFParsed
                    binding.highTemperature.text =
                        weatherData.forecastWeatherModel.forecastDayModel[0].dayModel.maxTempFParsed
                }

                withContext(Dispatchers.Main + excHandler) {
                    binding.locationText.text = weatherData.locationDataModel.shortLocation
                    binding.conditionText.text = weatherData.currentWeatherModel.conditionModel.text

                    // set new data list for hours weather today
                    adapterWeather.setList(weatherData.forecastWeatherModel.forecastDayModel[0].hourModel.filter {
                        // if date after current -> add to list on display weather
                        dateTimeUseCase.isDateAfterCurrent(dateTimeUseCase.timeEpochToDate(it.time))
                    }.map {
                        HourValueModel(
                            conditionModel = it.conditionModel,
                            temp = it.toMeasureDataTemperature(temperatureFormat),
                            time = it.time.toMeasureDataTime(timeFormat),
                        )
                    })
                }

            }
        }

    }

    // count min height to BottomSheet
    private fun getMinHeightBottomSheet(): Int {
        val buttonPanel = binding.bottomSheet.buttonPanel.run { y + height + marginBottom }
        return buttonPanel.toInt()
    }

    override fun changeCurrentLocation(location: LocationUserModel) {
        locationViewModel.updateActiveLocation(location)
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
            adView.adUnitId = ID_BOTTOM_SHEET_BANNER
            adView.setAdSize(adSize)
            adView.loadAd(AdRequest.Builder().build())
        }

    }

}