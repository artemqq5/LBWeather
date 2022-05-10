package com.example.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.MainActivity.Companion.main_context
import com.example.myapplication.R
import com.example.myapplication.adapter.CustomAdapter
import com.example.myapplication.databinding.FragmentDisplayWeatherBinding
import com.example.myapplication.helper.InternetConnection
import com.example.myapplication.helper.TimeFormat.DAYWEEK_DAY_MONTH_YEAR
import com.example.myapplication.helper.TimeFormat.HOUR_MINUTE
import com.example.myapplication.helper.TimeFormat.YEAR_MONTH_DAY
import com.example.myapplication.helper.TimeFormat.YEAR_MONTH_DAY_HOUR_MINUTE
import com.example.myapplication.helper.TimeFormat.getParsingTime
import com.example.myapplication.sharedPreferences.WeatherPref
import com.example.myapplication.viewmodel.ViewModelLocation
import com.example.myapplication.viewmodel.ViewModelWeather
import com.example.myapplication.weatherModelData.ConditionXX
import com.example.myapplication.weatherModelData.Hour
import com.example.myapplication.weatherModelData.WeatherModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.text.SimpleDateFormat
import java.util.*


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
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDisplayWeatherBinding.inflate(inflater)
        return binding.root
    }

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
            val locationCoordinate = WeatherPref.getShPrefLocation()?.let {
                "${it.lat},${it.lon}"
            } ?: "Kiev"
            updateData(locationCoordinate)
            Log.i("tytgyhu3j2", "request API from SwipeLayout")
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
                        it.current.humidity,
                        it.current.temp_c,
                        it.current.last_updated,
                        it.current.uv,
                        it.current.wind_kph
                    )
                )


            }

            // update list Weather
            adapterWeather.setList(listWeather)
        }

        // click listener to button details
        binding.detailButton.setOnClickListener {
            val bundleDay = bundleOf("day" to 0)
            findNavController().navigate(R.id.action_displayWeather_to_detailsDay, bundleDay)
        }


    }

    // method to add in list only future forecast
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
    private fun WeatherModel.updateCurrent() {

        val currentWeather = this.current

        binding.cityCountryInfo.text = "${this.location.name}, ${this.location.country}"
        binding.dateInfo.text = "${
            this.forecast.forecastday[0].date.getParsingTime(
                YEAR_MONTH_DAY,
                DAYWEEK_DAY_MONTH_YEAR
            )
        }"
        binding.currentTemperature.text = "${currentWeather.temp_c} °С"
        binding.textWeather.text = currentWeather.condition.text
        binding.speedWind.text = "${currentWeather.wind_kph}\nкм/год"
        binding.airIndex.text = "${currentWeather.air_quality.gb_defra_index}"
        binding.humidity.text = "${currentWeather.humidity} %"

    }

    private fun updateData(city: String) {
        if (!InternetConnection.checkForInternet(main_context)) {
            binding.swipeLayout.isRefreshing = false
            Toast.makeText(main_context, "Інтернет не працює", Toast.LENGTH_SHORT).show()
        } else {
            try {
                viewModel.gg(city, "uk") {
                    if(it == -1) {
                        Toast.makeText(main_context, "Помилка з'єднання", Toast.LENGTH_SHORT).show()
                    }
                    binding.swipeLayout.isRefreshing = false
                }
            } catch (e: Exception) {
                Toast.makeText(main_context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

}