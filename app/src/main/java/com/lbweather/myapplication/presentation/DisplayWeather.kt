package com.lbweather.myapplication.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.lbweather.myapplication.MyApp.Companion.logData
import com.lbweather.myapplication.databinding.FragmentDisplayWeatherBinding
import com.lbweather.myapplication.presentation.viewmodel.ViewModelWeather
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DisplayWeather : Fragment() {

    lateinit var binding: FragmentDisplayWeatherBinding

    private val viewModelWeather: ViewModelWeather by viewModel(ownerProducer = { requireActivity() })
//    private val viewModelLocation: ViewModelLocation by activityViewModels()

//    private val adapterWeather by lazy {
//        CustomAdapter(arrayListOf(), findNavController())
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDisplayWeatherBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModelWeather.flowDataWeather.collectLatest {

                binding.locationText.text = it.location.region
                binding.locationTemperature.text = it.current.temp_c.toString()
            }
        }

//
//        // bind adapter to recyclerView
//        binding.bottomSheetLayout.recyclerView.also {
//            it.adapter = adapterWeather
//        }
//
//        // create listener drawing element
//        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object :
//            OnGlobalLayoutListener {
//            override fun onGlobalLayout() {
//                // delete listener drawing element
//                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                // set state and minHeight to BottomSheet
//                BottomSheetBehavior.from(binding.bottomSheetLayout.bottomSheet).apply {
//                    peekHeight = getMinHeightBottomSheet()
//                    state = BottomSheetBehavior.STATE_COLLAPSED
//                }
//
//            }
//        })
//
//        // create listener on bottom sheet state change
//        BottomSheetBehavior.from(binding.bottomSheetLayout.bottomSheet)
//            .addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
//                override fun onStateChanged(bottomSheet: View, newState: Int) {
//                    if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
//                        binding.bottomSheetLayout.recyclerView.scrollToPosition(0)
//                    }
//                }
//
//                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
//
//            })
//
//        binding.swipeLayout.setOnRefreshListener {
//            val locationCoordinate = getShPrefLocation()?.locality ?: requireContext().fromStr(R.string.defaultCity)
//            updateData(locationCoordinate)
//        }
//
//
        // observe on liveData with WeatherModel object
//        viewModelWeather.weatherDataObject.observe(viewLifecycleOwner) { response ->
//
//            response?.let {
//                // set data to sharedPreferences
//                weatherProperty = it
//
//                // add data for future time in list Weather (SHOW IF IT FUTURE DATE)
//                it.setDataToday()
//
//                // update interface current display data
//                it.updateCurrent()
//            }
//
//            if (response == null) {
//                // show message about some error
//                    // if sharedPref not null show error
//                if (weatherProperty != null) {
//                    Toast.makeText(
//                        requireActivity(),
//                        requireContext().fromStr(R.string.errorConnection),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//
//            }
//
//            // stop refreshing
//            binding.swipeLayout.isRefreshing = false
//        }
//
//        // do request Weather API when Location has changed
//        viewModelLocation.currentLocation.observe(viewLifecycleOwner) {
//            it?.let {
//                setShPrefLocation(it)
//                updateData(it.locality)
//                viewModelLocation.updateListOfLocations(it)
//            }
//        }
//
//        viewModelLocation.listOfLocation.observe(viewLifecycleOwner) {
//            Log.i("myLog listOfLocation displayWeather", "$it")
//            setListLocationsPreference(it)
//        }
//
//        // click listener to button details
//        binding.detailButton.setOnClickListener {
//            findNavController().navigate(R.id.action_displayWeather_to_detailsDay)
//        }
//
//        binding.settingButton.setOnClickListener {
//            findNavController().navigate(R.id.action_displayWeather_to_settings_nav2)
//        }
//
//        // when user click on `Get Location Automatically`
//        binding.locationBox.setOnClickListener {
//            // Check location permission
//            // Check GPS on|off
//            // try to get last location
//            // show dialog with confirm location data fidelity
//            findNavController().navigate(R.id.action_displayWeather_to_dialogListLocations)
//        }
//
    }
//
//
//    // method to add in list only further forecast and CURRENT forecast
//    private fun WeatherModel.setDataToday() {
//
//        // get list of hour from current day
//        val hourList = this.forecast.forecastday[0].hour
//
//        // parse date
//        val timeNow = (this.location.localtime)
//            .getParsingTime(YEAR_MONTH_DAY_HOUR_MINUTE, HOUR_MINUTE)
//
//        val timeNowDate = SimpleDateFormat(HOUR_MINUTE, Locale.getDefault()).parse(timeNow)
//
//        // clear List with hour elements
//        listWeather.clear()
//
//        hourList.forEach { hour ->
//            val timeHour = hour.time.getParsingTime(YEAR_MONTH_DAY_HOUR_MINUTE, HOUR_MINUTE)
//            val timeHourData =
//                SimpleDateFormat(HOUR_MINUTE, Locale.getDefault()).parse(timeHour)
//
//            if (timeHourData!!.time > timeNowDate!!.time) {
//                listWeather.add(hour)
//            }
//
//        }
//
//        // add data for current time (ALWAYS SHOW)
//        listWeather.add(
//            0, Hour(
//                null,
//                null,
//                ConditionXX(this.current.condition.icon, this.current.condition.text),
//                this.current.feelslike_c,
//                this.current.feelslike_f,
//                this.current.humidity,
//                this.current.temp_c,
//                this.current.temp_f,
//                this.current.last_updated,
//                this.current.uv,
//                this.current.wind_kph,
//                this.current.wind_mph
//            )
//        )
//
//        // update list Weather
//        adapterWeather.setList(listWeather)
//
//    }
//
//    // count min height to BottomSheet
//    private fun getMinHeightBottomSheet(): Int {
//
//        val screen = binding.root
//        val buttonDetails = binding.detailButton
//
//        return (screen.height - buttonDetails.y - buttonDetails.height - buttonDetails.marginBottom).toInt()
//
//    }
//
//    // update current date display
//    @SuppressLint("SetTextI18n")
//    private fun WeatherModel.updateCurrent() {
//
//        val currentWeather = this.current
//
//        // set name of location (City, Country)
//        binding.cityCountryInfo.text = "${this.location.name}, ${this.location.country}"
//
//        // set current day
//        binding.dateInfo.text = this.forecast.forecastday[0].date.getParsingTime(
//            YEAR_MONTH_DAY, DAYWEEK_DAY_MONTH_YEAR
//        )
//
//        // set current temperature
//        binding.currentTemperature.text = if (requireContext().isCelsius()) {
//            "${currentWeather.temp_c} ${requireContext().fromStr(R.string.celsius)}"
//        } else "${currentWeather.temp_f} ${requireContext().fromStr(R.string.fahrenheit)}"
//
//        // set text about current weather
//        binding.textWeather.text = currentWeather.condition.text
//
//    }
//
//    private fun updateData(city: String) {
//        if (!requireActivity().checkForInternet()) {
//            binding.swipeLayout.isRefreshing = false
//            Toast.makeText(
//                requireActivity(),
//                requireContext().fromStr(R.string.internetNotWorking),
//                Toast.LENGTH_SHORT
//            )
//                .show()
//        } else {
//            viewModel.doRequestWeather(city, requireContext().fromStr(R.string.request_lang))
//        }
//    }
//
}