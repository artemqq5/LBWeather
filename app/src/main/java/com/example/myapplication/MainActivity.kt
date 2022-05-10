package com.example.myapplication

import android.os.Bundle
import android.util.Log
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
import com.example.myapplication.dialogs.DialogLocation
import com.example.myapplication.helper.InternetConnection
import com.example.myapplication.helper.LocationRequest
import com.example.myapplication.helper.locationModel.LocationModel
import com.example.myapplication.sharedPreferences.WeatherPref
import com.example.myapplication.viewmodel.ViewModelLocation
import com.example.myapplication.viewmodel.ViewModelWeather
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(),
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    private lateinit var navigationController: NavController
    private lateinit var navigationHost: NavHostFragment
    private val viewModelWeather: ViewModelWeather by viewModels()
    private val viewModelLocation: ViewModelLocation by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // assign a context
        main_context = this

        // set navigation fragmentContainer
        navigationController = (supportFragmentManager
            .findFragmentById(R.id.nav_fragment_controller) as NavHostFragment)
            .navController

        navigationHost = (supportFragmentManager
            .findFragmentById(R.id.nav_fragment_controller) as NavHostFragment)

        val navigation = findViewById<NavigationView>(R.id.navView)
        val toolBar = findViewById<MaterialToolbar>(R.id.toolBar)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

        // create configuration TollBar with navGraph, drawerLayout
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.displayWeather), drawerLayout)

        // bind navGraph, drawerLayout to ToolBar
        toolBar.setupWithNavController(navigationController, appBarConfiguration)
        // bind nav fragment to drawerLayout
        navigation.setupWithNavController(navigationController)

        // create listener on change layouts in nav fragmentContainer
        navigationController.addOnDestinationChangedListener { _, destination, _ ->
            val display = resources.getString(R.string.fragment_display)
            val settings = resources.getString(R.string.fragment_settings)
            val splashScreen = resources.getString(R.string.fragment_splash_screen)

            when (destination.label) {
                display -> {
                    findViewById<Button>(R.id.getLocationAuto).visibility = View.VISIBLE

                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    findViewById<AppBarLayout>(R.id.appBarLayout).visibility = View.VISIBLE
                }

                splashScreen -> {
                    findViewById<Button>(R.id.getLocationAuto).visibility = View.GONE

                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    findViewById<AppBarLayout>(R.id.appBarLayout).visibility = View.GONE
                }

                else -> {
                    findViewById<Button>(R.id.getLocationAuto).visibility = View.GONE

                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    findViewById<AppBarLayout>(R.id.appBarLayout).visibility = View.VISIBLE
                }
            }
        }

        // create listener on click on items in navDrawer
        navigation.setNavigationItemSelectedListener { menuItem ->

            val fragmentDisplayWeather = resources.getString(R.string.fragment_display)
            val fragmentSettings = resources.getString(R.string.fragment_settings)
            val fragmentDetailsData = resources.getString(R.string.fragment_details_day)
            val fragmentTomorrow = resources.getString(R.string.tomorrow)
            val fragmentDayAfterTomorrow = resources.getString(R.string.day_after_tomorrow)


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


        findViewById<Button>(R.id.getLocationAuto).setOnClickListener {

            LocationRequest.checkPermissionLocation { local->
                DialogLocation.dialogLocation(local) { locationFromFunction ->
                    viewModelLocation.updateCurrentLocation(locationFromFunction)
                }.show()
            }

        }

        viewModelLocation.currentLocation.observe(this) {
            updateDataLocation(it)
            Log.i("tytgyhu3j2", "request API from CurrentData")
        }


    }

    // logic in preference fragments
    override fun onPreferenceStartFragment(
        caller: PreferenceFragmentCompat,
        pref: Preference
    ): Boolean {

        return when (pref.key) {

            resources.getString(R.string.dataToDisplayPreference) -> {
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

        if (WeatherPref.getShPrefWeather() != null) {
            viewModelWeather.weatherDataObject.value = WeatherPref.getShPrefWeather()
        } else {
            val currentFragment = navigationController.currentBackStackEntry?.destination?.label
            val startFragment = navigationController.graph.startDestDisplayName
            if (currentFragment != startFragment) {
                navigationController.navigate(R.id.action_global_notConnection)
            }
        }

    }

    private fun updateDataLocation(modelLoc: LocationModel) {
        if (!InternetConnection.checkForInternet(main_context)) {
            Toast.makeText(main_context, "Інтернет не працює", Toast.LENGTH_SHORT).show()
        } else {
            try {
                val coordinate = "${modelLoc.lat},${modelLoc.lon}"
                viewModelWeather.gg(coordinate, "uk") {
                    if(it == 1) {
                        WeatherPref.setShPrefLocation(modelLoc)
                    } else Toast.makeText(this, "Помилка з'єднання", Toast.LENGTH_SHORT).show()

                }
            } catch (e: Exception) {
                Toast.makeText(main_context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

}
