package com.lbweather.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.lbweather.myapplication.databinding.ActivityMainBinding
import com.lbweather.myapplication.helper.FromStr.fromStr
import com.lbweather.myapplication.location.LocationModel
import com.lbweather.myapplication.location.LocationRequest
import com.lbweather.myapplication.network.internetConnection.InternetConnection.checkForInternet
import com.lbweather.myapplication.sharedPreferences.WeatherPref
import com.lbweather.myapplication.sharedPreferences.WeatherPref.getShPrefLocation
import com.lbweather.myapplication.sharedPreferences.WeatherPref.weatherProperty
import com.lbweather.myapplication.viewmodel.ViewModelLocation
import com.lbweather.myapplication.viewmodel.ViewModelWeather
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), View.OnClickListener,
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navigationController: NavController
    private val viewModelWeather: ViewModelWeather by viewModels()
    private val viewModelLocation: ViewModelLocation by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // assign a context
        main_context = this

        // set navigation fragmentContainer
        navigationController = (supportFragmentManager
            .findFragmentById(R.id.nav_fragment_controller) as NavHostFragment)
            .navController

        // initialisation request for get permission location
        LocationRequest.locationRequestInit()

        binding.buttonUpdateConnect.setOnClickListener(this)

        // do request Weather API when Location has changed
        viewModelLocation.currentLocation.observe(this) {
            updateDataLocation(it)
        }

    }

    // logic transition in Preference Fragments
    override fun onPreferenceStartFragment(
        caller: PreferenceFragmentCompat,
        pref: Preference
    ): Boolean {

        return when (pref.key) {

            fromStr(R.string.dataToDisplayPreference) -> {
                navigationController.navigate(R.id.action_settings_to_settingsDataDisplay)
                true
            }

            else -> false
        }

    }


    override fun onClick(p0: View?) {
        when (p0!!.id) {
            // try to update data again if hasn't data in locale storage
            R.id.buttonUpdateConnect -> {
                updateData()
            }

        }
    }


    companion object {
        lateinit var main_context: MainActivity
    }


    override fun onResume() {
        super.onResume()

        // If local storage has something data -> show them -> try to update data with internet
        // Else -> just update data with internet
        if (weatherProperty != null) {
            binding.containerMainActivity.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE

            updateDataBackground()
        } else {
            updateData()
        }

    }

    // Update data from new location object from `RequestLocationDialog`
    private fun updateDataLocation(modelLoc: LocationModel) {
        if (checkForInternet()) {
            viewModelWeather.doRequestWeather(
                modelLoc.locality,
                fromStr(R.string.request_lang)
            ) { result ->
                if (result) {
                    WeatherPref.setShPrefLocation(modelLoc)
                } else Toast.makeText(this, fromStr(R.string.errorConnection), Toast.LENGTH_SHORT)
                    .show()

            }
        } else {
            Toast.makeText(main_context, fromStr(R.string.internetNotWorking), Toast.LENGTH_SHORT)
                .show()
        }
    }

    // Display with loading...
    // Checking #1 - internet connection
    // Checking #2 - callback from request Weather API
    // If all is good -> go to `displayWeather`
    // Else -> show toast error and button reload
    private fun updateData() {
        binding.containerMainActivity.visibility = View.INVISIBLE
        binding.buttonUpdateConnect.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE

        val locationCoordinate = getShPrefLocation()?.locality ?: fromStr(R.string.defaultCity)

        if (checkForInternet()) {
            viewModelWeather.doRequestWeather(
                locationCoordinate,
                fromStr(R.string.request_lang)
            ) { result ->
                if (result) {
                    binding.containerMainActivity.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE

                } else {
                    Toast.makeText(this, fromStr(R.string.errorConnection), Toast.LENGTH_SHORT)
                        .show()
                    binding.buttonUpdateConnect.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }
            }
        } else {
            Toast.makeText(this, fromStr(R.string.internetNotWorking), Toast.LENGTH_SHORT)
                .show()
            binding.buttonUpdateConnect.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun updateDataBackground() {
        val locationCoordinate = getShPrefLocation()?.locality ?: fromStr(R.string.defaultCity)

        if (!checkForInternet()) {
            Toast.makeText(main_context, fromStr(R.string.internetNotWorking), Toast.LENGTH_SHORT)
                .show()
        } else {
            viewModelWeather.doRequestWeather(
                locationCoordinate,
                fromStr(R.string.request_lang)
            ) { result ->
                if (!result) {
                    Toast.makeText(
                        main_context,
                        fromStr(R.string.errorConnection),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


}
