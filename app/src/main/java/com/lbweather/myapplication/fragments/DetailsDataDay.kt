package com.lbweather.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.lbweather.myapplication.adapter.DaysAdapter
import com.lbweather.myapplication.databinding.FragmentDetailsDayBinding
import com.lbweather.myapplication.viewmodel.ViewModelWeather
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsDataDay : Fragment() {

    private lateinit var binding: FragmentDetailsDayBinding
    private val viewModelWeather: ViewModelWeather by activityViewModels()
    private val adapterWeather by lazy {
        DaysAdapter(emptyArray())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsDayBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewDays.adapter = adapterWeather


        viewModelWeather.weatherDataObject.observe(viewLifecycleOwner) { weatherModel ->
            weatherModel?.let {

                adapterWeather.setList(
                    arrayOf(
                       it.forecast.forecastday[0],
                        it.forecast.forecastday[1],
                        it.forecast.forecastday[2]
                    )
                )
            }
        }


    }
}