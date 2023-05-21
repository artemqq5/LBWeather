package com.lbweather.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.lbweather.myapplication.databinding.ActivityMainBinding
import com.lbweather.myapplication.other.helper.FromStr.fromStr
import com.lbweather.myapplication.presentation.viewmodel.ViewModelLocation
import com.lbweather.myapplication.presentation.viewmodel.ViewModelWeather
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(),
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navigationController: NavController
    private val viewModelWeather: ViewModelWeather by viewModel()
    private val viewModelLocation: ViewModelLocation by viewModel()

    private val excHandler = CoroutineExceptionHandler { _, throwable ->
        MyApp.logData("Coroutine Exception. MainActivity ($throwable)")
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
        lifecycleScope.launch(excHandler) {
            val lastCurrentLocationOrDefault = viewModelLocation.getLastCurrentLocation().name
            MyApp.logData("app is started ($lastCurrentLocationOrDefault)")
            viewModelWeather.getWeatherData(lastCurrentLocationOrDefault)
        }
    }

}
