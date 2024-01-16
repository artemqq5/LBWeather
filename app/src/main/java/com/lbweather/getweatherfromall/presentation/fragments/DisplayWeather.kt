package com.lbweather.getweatherfromall.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.lbweather.getweatherfromall.MyApp.Companion.logData
import com.lbweather.getweatherfromall.data.database.LocationTable
import com.lbweather.getweatherfromall.databinding.FragmentDisplayWeatherBinding
import com.lbweather.getweatherfromall.domain.model.weather.WeatherDataModel
import com.lbweather.getweatherfromall.presentation.NavigationInterfaceAdapter
import com.lbweather.getweatherfromall.presentation.adapters.CustomAdapter
import com.lbweather.getweatherfromall.presentation.viewmodel.WeatherViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.util.*


class DisplayWeather : Fragment(), NavigationInterfaceAdapter {

    private lateinit var binding: FragmentDisplayWeatherBinding

//    private val useCaseSettings: UseCaseSettings by inject()

    private val adapterWeather by lazy {
        CustomAdapter()
    }

//    private val locationAdapter by lazy {
//        LocationAdapter(arrayListOf(), this)
//    }


    private val weatherViewModel: WeatherViewModel by activityViewModel()

    private val excHandler = CoroutineExceptionHandler { _, throwable ->
        logData("Coroutine Exception. DisplayWeather ($throwable)")
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

        lifecycleScope.launch(excHandler) {
            weatherViewModel.weatherDataFlow.collectLatest { weatherData ->
                // disable updating swipe progress bar
                binding.swipeLayout.isRefreshing = false

                // set data to current weather
                setCurrentLocation(weatherData)

                // set new data list for hours weather today
                adapterWeather.setList(weatherData.forecastWeatherModel.forecastDayModel[0].hourModel)
            }
        }

//        // bind locations list adapter to recyclerView
//        binding.bottomSheet.recyclerLocation.adapter = locationAdapter
//
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

        // update weather
        weatherViewModel.updateWeather(lang = Locale.getDefault().language)

//
//        lifecycleScope.launch(excHandler) {
//            viewModelLocation.flowCurrentLocation.collectLatest {
//                it?.let {
//                    viewModelLocation.setLocationData(it)
//                    viewModelWeather.getWeatherData(it.name)
//                }
//            }
//        }
//
//        // observe to set location list to adapter
//        lifecycleScope.launch(excHandler) {
//            viewModelLocation.flowLocationList.collectLatest {
//                val list = it.map { itemList ->
//                    itemList.apply {
//                        statusActive =
//                            if (this.name == viewModelLocation.getLastCurrentLocation().name) {
//                                View.VISIBLE
//                            } else {
//                                View.INVISIBLE
//                            }
//                    }.also {
//                        logData("${itemList.name} - ${itemList.statusActive}")
//                    }
//                }
//
//                locationAdapter.setList(list.reversed())
//            }
//        }
//
//        // add swipe listener to delete item
//        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
//            override fun onMove(
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                target: RecyclerView.ViewHolder,
//            ): Boolean {
//                return false
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                if (locationAdapter.dataSet.size > 1) {
//                    lifecycleScope.launch(excHandler) {
//                        viewModelLocation.deleteLocationData(locationAdapter.dataSet[viewHolder.adapterPosition])
//                        if (viewHolder.adapterPosition == 0) {
//                            viewModelLocation.setCurrentLocationData(locationAdapter.dataSet[1])
//                        } else viewModelLocation.setCurrentLocationData(locationAdapter.dataSet[0])
//
//                    }
//                }
//            }
//
//        }).attachToRecyclerView(binding.bottomSheet.recyclerLocation)
//
//
//
//
//        // create listener on bottom sheet state change
//        BottomSheetBehavior.from(binding.bottomSheet.root)
//            .addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
//                override fun onStateChanged(bottomSheet: View, newState: Int) {
//                    if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
//                        binding.bottomSheet.recyclerView.scrollToPosition(0)
//                    }
//                }
//
//                override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                    binding.bottomSheet.menuLocations.alpha = 1 - slideOffset
//                    binding.bottomSheet.menuLocations.isEnabled = (1 - slideOffset >= 1)
//                }
//
//            })
//
//        // update data by swipe
//        binding.swipeLayout.setOnRefreshListener {
//            lifecycleScope.launch(excHandler) {
//                viewModelWeather.getWeatherData(viewModelLocation.getLastCurrentLocation().name)
//            }
//        }
//
//
//
//        // open fragment with search location
//        binding.bottomSheet.addLocation.setOnClickListener {
//            findNavController().navigate(R.id.action_displayWeather_to_dialogListLocations)
//        }
//
//        binding.bottomSheet.openSettings.setOnClickListener {
//            findNavController().navigate(R.id.action_displayWeather_to_settings)
//        }
//
//        // open bottom sheet with saved locations
//        binding.bottomSheet.menuLocations.setOnClickListener {
//            BottomSheetBehavior.from(binding.bottomSheet.root).state =
//                BottomSheetBehavior.STATE_EXPANDED
//        }

    }

    private fun setCurrentLocation(weatherData: WeatherDataModel) {
        binding.locationText.text = weatherData.locationDataModel.shortLocation
        binding.conditionText.text = weatherData.currentWeatherModel.conditionModel?.text
        binding.locationTemperature.text = weatherData.currentWeatherModel.tempC.toString()
        binding.lowTemperature.text =
            weatherData.forecastWeatherModel.forecastDayModel[0].dayModel?.minTempCParsed
        binding.highTemperature.text =
            weatherData.forecastWeatherModel.forecastDayModel[0].dayModel?.maxTempCParsed

        // set temp in unit that you choice (celsius or fahrenheit)
//        if (useCaseSettings.getTempUnit() == UseCaseSettings.TempUnit.CELSIUS) {

//        } else {
//            binding.locationTemperature.text = weatherData.current.tempFParsed
//            binding.lowTemperature.text = weatherData.forecast.forecastday[0].day.minTempFParsed
//            binding.highTemperature.text = weatherData.forecast.forecastday[0].day.maxTempFParsed
//        }
    }

    // count min height to BottomSheet
    private fun getMinHeightBottomSheet(): Int {
        val buttonPanel = binding.bottomSheet.buttonPanel.run { y + height + marginBottom }
        return buttonPanel.toInt()
    }

    override fun changeCurrentLocation(location: LocationTable) {
//        viewModelLocation.setCurrentLocationData(location)
    }
}