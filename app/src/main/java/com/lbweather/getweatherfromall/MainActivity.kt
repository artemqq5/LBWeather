package com.lbweather.getweatherfromall

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.snackbar.Snackbar
import com.lbweather.getweatherfromall.databinding.ActivityMainBinding
import com.lbweather.getweatherfromall.domain.network.ConnectionManager
import com.lbweather.getweatherfromall.other.helper.FromStr.fromStr
import com.lbweather.getweatherfromall.presentation.viewmodel.ViewModelInternet
import com.lbweather.getweatherfromall.presentation.viewmodel.ViewModelLocation
import com.lbweather.getweatherfromall.presentation.viewmodel.ViewModelWeather
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(),
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navigationController: NavController
    private val viewModelWeather: ViewModelWeather by viewModel()
    private val viewModelLocation: ViewModelLocation by viewModel()
    private val viewModelInternet: ViewModelInternet by viewModel()


    private val excHandler = CoroutineExceptionHandler { _, throwable ->
        MyApp.logData("Coroutine Exception. MainActivity ($throwable)")
    }

    private val snackBarInternet by lazy {
        Snackbar.make(binding.root, "No Internet connection", Snackbar.LENGTH_INDEFINITE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // assign a context
        main_context = this

        // set navigation fragmentContainer
        navigationController =
            (supportFragmentManager.findFragmentById(R.id.nav_fragment_controller) as NavHostFragment).navController

        viewModelLocation.apply {
            initLocationRequestPermission()
        }

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
        if (!viewModelInternet.getStatusNow()) {
            snackBarInternet.show()
        }

        lifecycleScope.launch {
            viewModelInternet.internetFlowData.collectLatest {
                if (it == ConnectionManager.StatusInternet.Available) {
                    snackBarInternet.dismiss()
                    lifecycleScope.launch(excHandler) {
                        val lastCurrentLocationOrDefault =
                            viewModelLocation.getLastCurrentLocation().name
                        MyApp.logData("app is started ($lastCurrentLocationOrDefault)")
                        viewModelWeather.getWeatherData(lastCurrentLocationOrDefault)
                    }
                } else {
                    snackBarInternet.show()
                }
            }
        }
    }

}
