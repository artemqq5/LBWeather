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
import com.lbweather.myapplication.network.internetConnection.checkForInternet
import com.lbweather.myapplication.sharedPreferences.WeatherPref.getShPrefLocation
import com.lbweather.myapplication.sharedPreferences.WeatherPref.weatherProperty
import com.lbweather.myapplication.viewmodel.ViewModelLocation
import com.lbweather.myapplication.viewmodel.ViewModelWeather
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
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
        navigationController =
            (supportFragmentManager.findFragmentById(R.id.nav_fragment_controller) as NavHostFragment).navController

        // initialisation request for get permission location
        viewModelLocation.initLocationRequest(this)
    }

    // logic transition in Preference Fragments
    override fun onPreferenceStartFragment(
        caller: PreferenceFragmentCompat, pref: Preference
    ): Boolean {

        return when (pref.key) {

            fromStr(R.string.dataToDisplayPreference) -> {
                navigationController.navigate(R.id.action_settings_to_settingsDataDisplay)
                true
            }

            else -> false
        }

    }


    companion object {
        lateinit var main_context: MainActivity
    }


    override fun onResume() {
        super.onResume()


        updateDataBackground()
    }


    // update data
    private fun updateDataBackground() {

        val locationCoordinate = getShPrefLocation()?.locality ?: fromStr(R.string.defaultCity)
        if (getShPrefLocation()?.locality.isNullOrEmpty()) viewModelLocation.currentLocation.value =
            (LocationModel("50.450001", "30.523333", fromStr(R.string.defaultCity)))

        if (!this.checkForInternet()) {
            Toast.makeText(this, fromStr(R.string.internetNotWorking), Toast.LENGTH_SHORT).show()
        } else {
            viewModelWeather.doRequestWeather(
                locationCoordinate, fromStr(R.string.request_lang)
            )
        }
    }


}
