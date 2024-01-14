package com.lbweather.getweatherfromall.presentation.parentDisplayFragment.futureForecastFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.lbweather.getweatherfromall.MyApp.Companion.logData
import com.lbweather.getweatherfromall.databinding.FragmentFutureForecastBinding
import com.lbweather.getweatherfromall.domain.UseCaseSettings
import com.lbweather.getweatherfromall.domain.model.weather.Hour
import com.lbweather.getweatherfromall.presentation.parentDisplayFragment.displayFragment.CustomAdapter
import com.lbweather.getweatherfromall.presentation.viewmodel.ViewModelLocation
import com.lbweather.getweatherfromall.presentation.viewmodel.ViewModelWeather
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FutureForecastFragment : Fragment() {

    private lateinit var binding: FragmentFutureForecastBinding

    private val viewModelWeather: ViewModelWeather by viewModel(ownerProducer = { requireActivity() })
    private val viewModelLocation: ViewModelLocation by viewModel(ownerProducer = { requireActivity() })

    private val useCaseSettings: UseCaseSettings by inject()

    private val adapterWeatherToday by lazy {
        CustomAdapter(arrayListOf(), useCaseSettings)
    }

    private val adapterWeatherTomorrow by lazy {
        CustomAdapter(arrayListOf(), useCaseSettings)
    }

    private val adapterWeatherAfterDayTomorrow by lazy {
        CustomAdapter(arrayListOf(), useCaseSettings)
    }

    private val excHandler = CoroutineExceptionHandler { _, throwable ->
        logData("Coroutine Exception. DisplayWeather ($throwable)")
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFutureForecastBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.todayList.adapter = adapterWeatherToday
        binding.tomorrowList.adapter = adapterWeatherTomorrow
        binding.afterTomorrowList.adapter = adapterWeatherAfterDayTomorrow

        lifecycleScope.launch(excHandler) {
            viewModelWeather.flowDataWeather.collectLatest {
                // disable updating swipe progress bar
                binding.swipeLayoutFuture.isRefreshing = false

                // set new data list for hours weather today
                adapterWeatherToday.setList(it.forecast.forecastday[0].hour as ArrayList<Hour>)

                // set new data list for hours weather tomorrow
                adapterWeatherTomorrow.setList(it.forecast.forecastday[1].hour as ArrayList<Hour>)

                // set new data list for hours weather after day tomorrow
                adapterWeatherAfterDayTomorrow.setList(it.forecast.forecastday[2].hour as ArrayList<Hour>)
            }
        }

        binding.swipeLayoutFuture.setOnRefreshListener {
            lifecycleScope.launch(excHandler) {
                viewModelWeather.getWeatherData(viewModelLocation.getLastCurrentLocation().name)
            }
        }

    }
}