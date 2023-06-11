package com.lbweather.getweatherfromall.presentation.displayFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.lbweather.getweatherfromall.MyApp.Companion.logData
import com.lbweather.getweatherfromall.R
import com.lbweather.getweatherfromall.data.database.LocationTable
import com.lbweather.getweatherfromall.databinding.FragmentDisplayWeatherBinding
import com.lbweather.getweatherfromall.domain.UseCaseSettings
import com.lbweather.getweatherfromall.domain.model.weather.Hour
import com.lbweather.getweatherfromall.helper.TimeFormat.compareDate
import com.lbweather.getweatherfromall.presentation.locationsFragment.LocationAdapter
import com.lbweather.getweatherfromall.presentation.locationsFragment.NavigationInterfaceAdapter
import com.lbweather.getweatherfromall.presentation.viewmodel.ViewModelLocation
import com.lbweather.getweatherfromall.presentation.viewmodel.ViewModelWeather
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class DisplayWeather : Fragment(), NavigationInterfaceAdapter {

    private lateinit var binding: FragmentDisplayWeatherBinding

    private val viewModelWeather: ViewModelWeather by viewModel(ownerProducer = { requireActivity() })
    private val viewModelLocation: ViewModelLocation by viewModel(ownerProducer = { requireActivity() })

    private val useCaseSettings: UseCaseSettings by inject()

    private val mutableMinBottomSheetHeightListener = MutableSharedFlow<Int>()
    private val minBottomSheetHeightListener: Flow<Int>
        get() = mutableMinBottomSheetHeightListener.distinctUntilChanged()

    private val adapterWeather by lazy {
        CustomAdapter(arrayListOf(), useCaseSettings)
    }

    private val locationAdapter by lazy {
        LocationAdapter(arrayListOf(), this)
    }

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

        // bind locations list adapter to recyclerView
        binding.bottomSheet.recyclerLocation.adapter = locationAdapter

        // bind adapter to recyclerView
        binding.bottomSheet.recyclerView.also {
            it.adapter = adapterWeather
            it.viewTreeObserver.addOnGlobalLayoutListener {
                lifecycleScope.launch {
                    mutableMinBottomSheetHeightListener.emit(getMinHeightBottomSheet())
                }
            }
        }

        lifecycleScope.launch(excHandler) {
            viewModelWeather.flowDataWeather.collectLatest {
                // disable updating swipe progress bar
                binding.swipeLayout.isRefreshing = false

                // set data to current weather
                binding.locationText.text = it.location.name
                binding.conditionText.text = it.current.condition.text

                // set temp in unit that you choice (celsius or fahrenheit)
                if (useCaseSettings.getTempUnit() == UseCaseSettings.TempUnit.CELSIUS) {
                    binding.locationTemperature.text = it.current.tempCParsed
                    binding.lowTemperature.text = it.forecast.forecastday[0].day.minTempCParsed
                    binding.highTemperature.text = it.forecast.forecastday[0].day.maxTempCParsed
                } else {
                    binding.locationTemperature.text = it.current.tempFParsed
                    binding.lowTemperature.text = it.forecast.forecastday[0].day.minTempFParsed
                    binding.highTemperature.text = it.forecast.forecastday[0].day.maxTempFParsed
                }

                // set new data list for hours weather today
                adapterWeather.setList(it.forecast.forecastday[0].hour.filter { hour ->
                    compareDate(hour.time)
                } as ArrayList<Hour>)
            }
        }

        lifecycleScope.launch(excHandler) {
            viewModelLocation.flowCurrentLocation.collectLatest {
                it?.let {
                    viewModelLocation.setLocationData(it)
                    viewModelWeather.getWeatherData(it.name)
                }
            }
        }

        // observe to set location list to adapter
        lifecycleScope.launch(excHandler) {
            viewModelLocation.flowLocationList.collectLatest {
                val list = it.map { itemList ->
                    itemList.apply {
                        statusActive =
                            if (this.name == viewModelLocation.getLastCurrentLocation().name) {
                                View.VISIBLE
                            } else {
                                View.INVISIBLE
                            }
                    }.also {
                        logData("${itemList.name} - ${itemList.statusActive}")
                    }
                }

                locationAdapter.setList(list.reversed())
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
                if (locationAdapter.dataSet.size > 1) {
                    lifecycleScope.launch(excHandler) {
                        viewModelLocation.deleteLocationData(locationAdapter.dataSet[viewHolder.adapterPosition])
                        if (viewHolder.adapterPosition == 0) {
                            viewModelLocation.setCurrentLocationData(locationAdapter.dataSet[1])
                        } else viewModelLocation.setCurrentLocationData(locationAdapter.dataSet[0])

                    }
                }
            }

        }).attachToRecyclerView(binding.bottomSheet.recyclerLocation)


        // listener for draw elements
        binding.bottomSheet.root.post {
            lifecycleScope.launch {
                minBottomSheetHeightListener.collectLatest {
                    BottomSheetBehavior.from(binding.bottomSheet.root).apply {
                        peekHeight = it // calculate min height
                        state = BottomSheetBehavior.STATE_COLLAPSED
                    }

                    logData("myLoggerBottomSheet = $it")
                    delay(1L) // to hide resize peekHeight from default to custom
                    binding.bottomSheet.root.visibility = View.VISIBLE
                }
            }
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
                    binding.bottomSheet.menuLocations.alpha = 1 - slideOffset
                    binding.bottomSheet.menuLocations.isEnabled = (1 - slideOffset >= 1)
                }

            })

        // update data by swipe
        binding.swipeLayout.setOnRefreshListener {
            lifecycleScope.launch(excHandler) {
                viewModelWeather.getWeatherData(viewModelLocation.getLastCurrentLocation().name)
            }
        }


        // open fragment with search location
        binding.bottomSheet.addLocation.setOnClickListener {
            findNavController().navigate(R.id.action_displayWeather_to_dialogListLocations)
        }

        binding.bottomSheet.openSettings.setOnClickListener {
            findNavController().navigate(R.id.action_displayWeather_to_settings)
        }

        // open bottom sheet with saved locations
        binding.bottomSheet.menuLocations.setOnClickListener {
            BottomSheetBehavior.from(binding.bottomSheet.root).state =
                BottomSheetBehavior.STATE_EXPANDED
        }

    }

    // count min height to BottomSheet
    private fun getMinHeightBottomSheet(): Int {
        val buttonPanel = binding.bottomSheet.buttonPanel.run { y + height + marginBottom }
        return buttonPanel.toInt()
    }

    override fun changeCurrentLocation(location: LocationTable) {
        viewModelLocation.setCurrentLocationData(location)
    }
}