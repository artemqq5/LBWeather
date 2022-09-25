package com.lbweather.myapplication.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.lbweather.myapplication.MainActivity.Companion.main_context
import com.lbweather.myapplication.R
import com.lbweather.myapplication.adapter.CustomAdapter
import com.lbweather.myapplication.databinding.FragmentDisplayWeatherBinding
import com.lbweather.myapplication.dialogs.DialogInfo.dialogInfo
import com.lbweather.myapplication.dialogs.DialogLocation
import com.lbweather.myapplication.helper.FromStr.fromStr
import com.lbweather.myapplication.helper.StateUnit.isCelsius
import com.lbweather.myapplication.helper.StateUnit.isKilometer
import com.lbweather.myapplication.helper.TimeFormat.DAYWEEK_DAY_MONTH_YEAR
import com.lbweather.myapplication.helper.TimeFormat.HOUR_MINUTE
import com.lbweather.myapplication.helper.TimeFormat.YEAR_MONTH_DAY
import com.lbweather.myapplication.helper.TimeFormat.YEAR_MONTH_DAY_HOUR_MINUTE
import com.lbweather.myapplication.helper.TimeFormat.getParsingTime
import com.lbweather.myapplication.location.LocationRequest
import com.lbweather.myapplication.network.internetConnection.InternetConnection
import com.lbweather.myapplication.sharedPreferences.WeatherPref.getShPrefLocation
import com.lbweather.myapplication.viewmodel.ViewModelLocation
import com.lbweather.myapplication.viewmodel.ViewModelWeather
import com.lbweather.myapplication.weatherModelData.ConditionXX
import com.lbweather.myapplication.weatherModelData.Hour
import com.lbweather.myapplication.weatherModelData.WeatherModel
import dagger.hilt.android.AndroidEntryPoint
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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

                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }

            })

        binding.swipeLayout.setOnRefreshListener {
            val locationCoordinate = getShPrefLocation()?.locality ?: fromStr(R.string.defaultCity)
            updateData(locationCoordinate)
        }



        // observe on liveData with WeatherModel object
        viewModel.weatherDataObject.observe(viewLifecycleOwner) { response ->

            // clear List with hour elements
            listWeather.clear()

            response?.let {

                // update interface current display data
                it.updateCurrent()

                // get list of hour from current day
                val today = it.forecast.forecastday[0].hour

                // parse date
                val timeNow =
                    it.location.localtime.getParsingTime(YEAR_MONTH_DAY_HOUR_MINUTE, HOUR_MINUTE)
                val timeNowDate = SimpleDateFormat(HOUR_MINUTE, Locale.getDefault()).parse(timeNow)

                // add data for future time in list Weather
                today.setDataToTime(timeNowDate)

                // add data for current time
                listWeather.add(
                    0,
                    Hour(
                        null,
                        null,
                        ConditionXX(it.current.condition.icon, it.current.condition.text),
                        it.current.feelslike_c,
                        it.current.feelslike_f,
                        it.current.humidity,
                        it.current.temp_c,
                        it.current.temp_f,
                        it.current.last_updated,
                        it.current.uv,
                        it.current.wind_kph,
                        it.current.wind_mph
                    )
                )


            }

            // update list Weather
            adapterWeather.setList(listWeather)
        }


        // click listener to button details
        binding.detailButton.setOnClickListener {
            findNavController().navigate(R.id.action_displayWeather_to_detailsDay)
        }

        binding.settingButton.setOnClickListener {
            findNavController().navigate(R.id.action_displayWeather_to_settings_nav2)
        }

        binding.infoButton.setOnClickListener {
            dialogInfo {
                // callback
            }.show()
        }

        // when user click on `Get Location Automatically`
        binding.cityCountryInfo.setOnClickListener {
            // Check location permission
            // Check GPS on|off
            // try to get last location
            // show dialog with confirm location data fidelity
            LocationRequest.checkPermissionLocation { local ->
                DialogLocation.dialogLocation(local) { locationFromFunction ->
                    viewModelLocation.updateCurrentLocation(locationFromFunction)
                }.show()
            }
        }

    }


    // method to add in list only further forecast
    private fun List<Hour>.setDataToTime(timeNowDate: Date?) {

        this.forEach { hour ->
            val timeHour = hour.time.getParsingTime(YEAR_MONTH_DAY_HOUR_MINUTE, HOUR_MINUTE)
            val timeHourData = SimpleDateFormat(HOUR_MINUTE, Locale.getDefault()).parse(timeHour)

            if (timeHourData!!.time > timeNowDate!!.time) {
                listWeather.add(hour)
            }

        }

    }

    // count min height to BottomSheet
    private fun getMinHeightBottomSheet(): Int {

        val heightScreen = binding.ffff.height
        val buttonDetails = binding.detailButton

        return heightScreen - buttonDetails.y.toInt() - buttonDetails.height

    }

    // update current date display
    @SuppressLint("SetTextI18n")
    private fun WeatherModel.updateCurrent() {

        val currentWeather = this.current

        binding.cityCountryInfo.text = "${this.location.name}, ${this.location.country}"
        binding.dateInfo.text = this.forecast.forecastday[0].date.getParsingTime(
            YEAR_MONTH_DAY,
            DAYWEEK_DAY_MONTH_YEAR
        )

        binding.currentTemperature.text = if (isCelsius()) {
            "${currentWeather.temp_c} ${fromStr(R.string.celsius)}"
        } else "${currentWeather.temp_f} ${fromStr(R.string.fahrenheit)}"

        binding.speedWind.text = if (isKilometer()) {
            "${currentWeather.wind_kph} ${fromStr(R.string.kph)}"
        } else "${currentWeather.wind_mph} ${fromStr(R.string.mph)}"

        binding.textWeather.text = currentWeather.condition.text
        binding.uvIndex.text = "${currentWeather.uv}"
        binding.humidity.text = "${currentWeather.humidity} %"

    }

    private fun updateData(city: String) {
        if (!InternetConnection.checkForInternet()) {
            binding.swipeLayout.isRefreshing = false
            Toast.makeText(main_context, fromStr(R.string.internetNotWorking), Toast.LENGTH_SHORT)
                .show()
        } else {
            viewModel.doRequestWeather(
                city,
                fromStr(R.string.request_lang)
            ) { result ->
                if (!result) {
                    Toast.makeText(
                        main_context,
                        fromStr(R.string.errorConnection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                binding.swipeLayout.isRefreshing = false
            }
        }
    }

}