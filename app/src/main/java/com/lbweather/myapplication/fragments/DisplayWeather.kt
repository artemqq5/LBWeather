package com.lbweather.myapplication.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Toast
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.lbweather.myapplication.R
import com.lbweather.myapplication.adapter.CustomAdapter
import com.lbweather.myapplication.databinding.FragmentDisplayWeatherBinding
import com.lbweather.myapplication.helper.FromStr.fromStr
import com.lbweather.myapplication.helper.StateUnit.isCelsius
import com.lbweather.myapplication.helper.TimeFormat.DAYWEEK_DAY_MONTH_YEAR
import com.lbweather.myapplication.helper.TimeFormat.HOUR_MINUTE
import com.lbweather.myapplication.helper.TimeFormat.YEAR_MONTH_DAY
import com.lbweather.myapplication.helper.TimeFormat.YEAR_MONTH_DAY_HOUR_MINUTE
import com.lbweather.myapplication.helper.TimeFormat.getParsingTime
import com.lbweather.myapplication.network.internetConnection.checkForInternet
import com.lbweather.myapplication.sharedPreferences.WeatherPref.getShPrefLocation
import com.lbweather.myapplication.sharedPreferences.WeatherPref.setListLocationsPreference
import com.lbweather.myapplication.sharedPreferences.WeatherPref.setShPrefLocation
import com.lbweather.myapplication.sharedPreferences.WeatherPref.weatherProperty
import com.lbweather.myapplication.viewmodel.ViewModelLocation
import com.lbweather.myapplication.viewmodel.ViewModelWeather
import com.lbweather.myapplication.weatherModelData.ConditionXX
import com.lbweather.myapplication.weatherModelData.Hour
import com.lbweather.myapplication.weatherModelData.WeatherModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class DisplayWeather : Fragment() {

    lateinit var binding: FragmentDisplayWeatherBinding

    private val viewModel: ViewModelWeather by activityViewModels()
    private val viewModelLocation: ViewModelLocation by activityViewModels()

    private val listWeather = arrayListOf<Hour>()
    private val adapterWeather by lazy {
        CustomAdapter(listWeather, findNavController())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDisplayWeatherBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // bind adapter to recyclerView
        binding.bottomSheetLayout.recyclerView.also {
            it.adapter = adapterWeather
        }

        // create listener drawing element
        binding.detailButton.viewTreeObserver.addOnGlobalLayoutListener(object :
            OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // delete listener drawing element
                binding.detailButton.viewTreeObserver.removeOnGlobalLayoutListener(this)
                // set state and minHeight to BottomSheet
                BottomSheetBehavior.from(binding.bottomSheetLayout.bottomSheet).apply {
                    peekHeight = getMinHeightBottomSheet()
                    state = BottomSheetBehavior.STATE_COLLAPSED
                }

            }
        })

        // create listener on bottom sheet state change
        BottomSheetBehavior.from(binding.bottomSheetLayout.bottomSheet)
            .addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        binding.bottomSheetLayout.recyclerView.scrollToPosition(0)
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}

            })

        binding.swipeLayout.setOnRefreshListener {
            val locationCoordinate = getShPrefLocation()?.locality ?: fromStr(R.string.defaultCity)
            updateData(locationCoordinate)
        }


        // observe on liveData with WeatherModel object
        viewModel.weatherDataObject.observe(viewLifecycleOwner) { response ->

            response?.let {
                // set data to sharedPreferences
                weatherProperty = it

                // add data for future time in list Weather (SHOW IF IT FUTURE DATE)
                it.setDataToday()

                // update interface current display data
                it.updateCurrent()
            }

            if (response == null) {
                // show message about some error
                    // if sharedPref not null show error
                if (weatherProperty != null) {
                    Toast.makeText(
                        requireActivity(),
                        fromStr(R.string.errorConnection),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            // stop refreshing
            binding.swipeLayout.isRefreshing = false
        }

        // do request Weather API when Location has changed
        viewModelLocation.currentLocation.observe(viewLifecycleOwner) {
            it?.let {
                setShPrefLocation(it)
                updateData(it.locality)
                viewModelLocation.updateListOfLocations(it)
            }
        }

        viewModelLocation.listOfLocation.observe(viewLifecycleOwner) {
            Log.i("myLog listOfLocation displayWeather", "$it")
            setListLocationsPreference(it)
        }

        // click listener to button details
        binding.detailButton.setOnClickListener {
            findNavController().navigate(R.id.action_displayWeather_to_detailsDay)
        }

        binding.settingButton.setOnClickListener {
            findNavController().navigate(R.id.action_displayWeather_to_settings_nav2)
        }

        // when user click on `Get Location Automatically`
        binding.locationBox.setOnClickListener {
            // Check location permission
            // Check GPS on|off
            // try to get last location
            // show dialog with confirm location data fidelity
            findNavController().navigate(R.id.action_displayWeather_to_dialogListLocations)
        }

    }


    // method to add in list only further forecast and CURRENT forecast
    private fun WeatherModel.setDataToday() {

        // get list of hour from current day
        val hourList = this.forecast.forecastday[0].hour

        // parse date
        val timeNow = (this.location.localtime)
            .getParsingTime(YEAR_MONTH_DAY_HOUR_MINUTE, HOUR_MINUTE)

        val timeNowDate = SimpleDateFormat(HOUR_MINUTE, Locale.getDefault()).parse(timeNow)

        // clear List with hour elements
        listWeather.clear()

        hourList.forEach { hour ->
            val timeHour = hour.time.getParsingTime(YEAR_MONTH_DAY_HOUR_MINUTE, HOUR_MINUTE)
            val timeHourData =
                SimpleDateFormat(HOUR_MINUTE, Locale.getDefault()).parse(timeHour)

            if (timeHourData!!.time > timeNowDate!!.time) {
                listWeather.add(hour)
            }

        }

        // add data for current time (ALWAYS SHOW)
        listWeather.add(
            0, Hour(
                null,
                null,
                ConditionXX(this.current.condition.icon, this.current.condition.text),
                this.current.feelslike_c,
                this.current.feelslike_f,
                this.current.humidity,
                this.current.temp_c,
                this.current.temp_f,
                this.current.last_updated,
                this.current.uv,
                this.current.wind_kph,
                this.current.wind_mph
            )
        )

        // update list Weather
        adapterWeather.setList(listWeather)

    }

    // count min height to BottomSheet
    private fun getMinHeightBottomSheet(): Int {

        val heightScreen = binding.root
        val buttonDetails = binding.detailButton

        return (heightScreen.height - buttonDetails.y - buttonDetails.height - buttonDetails.marginBottom).toInt()

    }

    // update current date display
    @SuppressLint("SetTextI18n")
    private fun WeatherModel.updateCurrent() {

        val currentWeather = this.current

        // set name of location (City, Country)
        binding.cityCountryInfo.text = "${this.location.name}, ${this.location.country}"

        // set current day
        binding.dateInfo.text = this.forecast.forecastday[0].date.getParsingTime(
            YEAR_MONTH_DAY, DAYWEEK_DAY_MONTH_YEAR
        )

        // set current temperature
        binding.currentTemperature.text = if (isCelsius()) {
            "${currentWeather.temp_c} ${fromStr(R.string.celsius)}"
        } else "${currentWeather.temp_f} ${fromStr(R.string.fahrenheit)}"

        // set text about current weather
        binding.textWeather.text = currentWeather.condition.text

    }

    private fun updateData(city: String) {
        if (!requireActivity().checkForInternet()) {
            binding.swipeLayout.isRefreshing = false
            Toast.makeText(
                requireActivity(),
                fromStr(R.string.internetNotWorking),
                Toast.LENGTH_SHORT
            )
                .show()
        } else {
            viewModel.doRequestWeather(city, fromStr(R.string.request_lang))
        }
    }

}