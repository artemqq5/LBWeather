package com.lbweather.myapplication.presentation.displayFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.lbweather.myapplication.MyApp
import com.lbweather.myapplication.R
import com.lbweather.myapplication.databinding.FragmentDisplayWeatherBinding
import com.lbweather.myapplication.domain.model.weather.Hour
import com.lbweather.myapplication.other.helper.TimeFormat.compareDate
import com.lbweather.myapplication.presentation.viewmodel.ViewModelLocation
import com.lbweather.myapplication.presentation.viewmodel.ViewModelWeather
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class DisplayWeather : Fragment() {

    lateinit var binding: FragmentDisplayWeatherBinding

    private val viewModelWeather: ViewModelWeather by viewModel(ownerProducer = { requireActivity() })
    private val viewModelLocation: ViewModelLocation by viewModel(ownerProducer = { requireActivity() })

    private val adapterWeather by lazy {
        CustomAdapter(arrayListOf())
    }

    private val excHandler = CoroutineExceptionHandler { _, throwable ->
        MyApp.logData("Coroutine Exception. DisplayWeather ($throwable)")
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

        // bind adapter to recyclerView
        binding.bottomSheet.recyclerView.also {
            it.adapter = adapterWeather
        }

        lifecycleScope.launch(excHandler) {
            viewModelWeather.flowDataWeather.collectLatest {
                // disable updating swipe progress bar
                if (binding.swipeLayout.isRefreshing) binding.swipeLayout.isRefreshing = false

                // set data to current weather
                binding.locationText.text = it.location.name
                binding.locationTemperature.text = it.current.tempCParsed
                binding.conditionText.text = it.current.condition.text
                binding.lowTemperature.text = it.forecast.forecastday[0].day.minTempCParsed
                binding.highTemperature.text = it.forecast.forecastday[0].day.maxTempCParsed

                // set new data list for hours weather today
                adapterWeather.setList(it.forecast.forecastday[0].hour.filter { hour ->
                    compareDate(hour.time)
                } as ArrayList<Hour>)

                // reset active status in bottom sheet if new location not the same
                if (it.location.locationField != binding.bottomSheet.locationLayout.cityCountryInfo.text) {
                    binding.bottomSheet.locationLayout.statusUse.visibility = View.INVISIBLE
                }
            }
        }

        lifecycleScope.launch(excHandler) {
            viewModelLocation.flowCurrentLocation.collectLatest {
                it?.let {
                    viewModelLocation.setLocationData(it)

                    if (it.locationField == binding.bottomSheet.locationLayout.cityCountryInfo.text) {
                        binding.bottomSheet.locationLayout.statusUse.visibility = View.VISIBLE
                    }

                    viewModelWeather.getWeatherData(it.name)
                }
            }
        }


        // listener for draw elements
        binding.bottomSheet.root.post {
            BottomSheetBehavior.from(binding.bottomSheet.root).apply {
                peekHeight = getMinHeightBottomSheet() // calculate min height
                state = BottomSheetBehavior.STATE_COLLAPSED
            }

            binding.bottomSheet.root.visibility = View.VISIBLE
        }


        // create listener on bottom sheet state change
        BottomSheetBehavior.from(binding.bottomSheet.root)
            .addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        binding.bottomSheet.recyclerView.scrollToPosition(0)
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    binding.bottomSheet.addLocation.alpha = 1 - slideOffset
                    binding.bottomSheet.addLocation.isEnabled = (1 - slideOffset >= 1)
                }

            })

        // update data by swipe
        binding.swipeLayout.setOnRefreshListener {
            lifecycleScope.launch(excHandler) {
                viewModelWeather.getWeatherData(viewModelLocation.getLastCurrentLocation().name)
            }
        }

        // open bottom sheet with button menu
        binding.bottomSheet.addLocation.setOnClickListener {
            BottomSheetBehavior.from(binding.bottomSheet.root).state =
                BottomSheetBehavior.STATE_EXPANDED
        }

        // search location by user search
        lifecycleScope.launch(excHandler) {
            viewModelWeather.flowDataLocation.collectLatest {
                binding.bottomSheet.locationLayout.apply {
                    root.visibility = View.VISIBLE
                    cityCountryInfo.text = it.location.locationField

                    binding.bottomSheet.locationLayout.statusUse.visibility =
                        if (viewModelLocation.getLastCurrentLocation().name == it.location.name) View.VISIBLE
                        else View.INVISIBLE

                    // set this location
                    root.setOnClickListener { _ ->
                        statusUse.visibility = View.VISIBLE
                        viewModelLocation.setCurrentLocationData(it.location)
                    }
                }


            }
        }

        // search location by text input
        binding.bottomSheet.searchButton.setOnClickListener {
            viewModelWeather.getLocationData(
                location = binding.bottomSheet.locationInputField.editText?.text.toString()
            )
        }

        // enable/disable button search by textField consists
        binding.bottomSheet.locationInputField.editText?.addTextChangedListener {
            binding.bottomSheet.searchButton.isEnabled = !(it.isNullOrEmpty())
        }

        // open fragment with saved locations
        binding.bottomSheet.menuLocations.setOnClickListener {
            findNavController().navigate(R.id.action_displayWeather_to_dialogListLocations)
        }

    }

    // count min height to BottomSheet
    private fun getMinHeightBottomSheet(): Int {
        val buttonPanel = binding.bottomSheet.buttonPanel.run { y + height + marginBottom }
        return buttonPanel.toInt()
    }
}