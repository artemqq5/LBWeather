package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.dialogs.DialogLocation.dialogLocation
import com.example.myapplication.helper.FromStr.fromStr
import com.example.myapplication.location.LocationModel
import com.example.myapplication.location.LocationRequest
import com.example.myapplication.location.LocationRequest.checkPermissionLocation
import com.example.myapplication.network.internetConnection.InternetConnection.checkForInternet
import com.example.myapplication.sharedPreferences.WeatherPref
import com.example.myapplication.sharedPreferences.WeatherPref.getShPrefLocation
import com.example.myapplication.sharedPreferences.WeatherPref.getShPrefWeather
import com.example.myapplication.viewmodel.ViewModelLocation
import com.example.myapplication.viewmodel.ViewModelWeather

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

        val navigation = binding.navView
        val toolBar = binding.toolBar
        val drawerLayout = binding.drawerLayout

        // create configuration TollBar with navGraph, drawerLayout
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.displayWeather), drawerLayout)

        // bind navGraph, drawerLayout to ToolBar
        toolBar.setupWithNavController(navigationController, appBarConfiguration)
        // bind nav fragment to drawerLayout
        navigation.setupWithNavController(navigationController)

        // create listener on change layouts in nav fragmentContainer
        navigationController.addOnDestinationChangedListener { _, destination, _ ->
            val display = fromStr(R.string.fragment_display)

            when (destination.label) {
                display -> {
                    findViewById<Button>(R.id.getLocationAuto).visibility = View.VISIBLE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                }

                else -> {
                    findViewById<Button>(R.id.getLocationAuto).visibility = View.GONE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
            }
        }

        // create listener on click on items in navDrawer
        navigation.setNavigationItemSelectedListener { menuItem ->

            val fragmentTomorrow = fromStr(R.string.tomorrow)
            val fragmentDayAfterTomorrow = fromStr(R.string.day_after_tomorrow)


            when (menuItem.title) {
                fragmentTomorrow -> {
                    val bundleTomorrow = bundleOf("day" to 1)
                    navigationController.navigate(
                        R.id.action_displayWeather_to_detailsDay,
                        bundleTomorrow
                    )
                }

                fragmentDayAfterTomorrow -> {
                    val bundleDayAfterTomorrow = bundleOf("day" to 2)
                    navigationController.navigate(
                        R.id.action_displayWeather_to_detailsDay,
                        bundleDayAfterTomorrow
                    )
                }

                // use default actions in navGraph if haven't custom actions
                else ->
                    NavigationUI.onNavDestinationSelected(menuItem, navigationController)
            }

            true
        }

        // initialisation request for get permission location
        LocationRequest.locationRequestInit()

        binding.getLocationAuto.setOnClickListener(this)
        binding.buttonUpdateConnect.setOnClickListener(this)

        // do request Weather API when Location has changed
        viewModelLocation.currentLocation.observe(this) {
            updateDataLocation(it)
            binding.getLocationAuto.text = it.locality
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

            // when user click on `Get Location Automatically`
            R.id.getLocationAuto -> {
                // Check location permission
                // Check GPS on|off
                // try to get last location
                // show dialog with confirm location data fidelity
                checkPermissionLocation { local ->
                    dialogLocation(local) { locationFromFunction ->
                        viewModelLocation.updateCurrentLocation(locationFromFunction)
                    }.show()
                }

            }

        }
    }


    companion object {
        lateinit var main_context: MainActivity
    }


    override fun onResume() {
        super.onResume()

        // set location name to button `getAutoLocation`
        setLocationName()

        // If local storage has something data -> show them -> try to update data with internet
        // Else -> just update data with internet
        if (getShPrefWeather() != null) {
            binding.containerMainActivity.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
            viewModelWeather.weatherDataObject.value = getShPrefWeather()
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
    // If all are good -> go to `displayWeather`
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
            Toast.makeText(main_context, fromStr(R.string.internetNotWorking), Toast.LENGTH_SHORT)
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

    private fun setLocationName() {
        binding.getLocationAuto.text =
            getShPrefLocation()?.locality ?: fromStr(R.string.defaultCity)
    }


}
