package com.lbweather.getweatherfromall.presentation.locationsFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.lbweather.getweatherfromall.MyApp.Companion.logData
import com.lbweather.getweatherfromall.R
import com.lbweather.getweatherfromall.databinding.DialogLocationsBinding
import com.lbweather.getweatherfromall.presentation.viewmodel.ViewModelLocation
import com.lbweather.getweatherfromall.presentation.viewmodel.ViewModelWeather
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DialogListLocations : DialogFragment() {

    private lateinit var binding: DialogLocationsBinding

    private val viewModelLocation: ViewModelLocation by viewModel(ownerProducer = { requireActivity() })
    private val viewModelWeather: ViewModelWeather by viewModel(ownerProducer = { requireActivity() })

    private val excHandler = CoroutineExceptionHandler { _, throwable ->
        logData("Coroutine Exception. DialogListLocations ($throwable)")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DialogLocationsBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch(excHandler) {
            viewModelLocation.locationFromGPS.collectLatest {
                binding.locationLayout.apply {
                    root.visibility = View.VISIBLE
                    cityCountryInfo.text = it.locationField

                    binding.locationLayout.statusUse.visibility =
                        if (viewModelLocation.getLastCurrentLocation().name == it.name) View.VISIBLE
                        else View.INVISIBLE

                    // set this location
                    root.setOnClickListener { _ ->
                        statusUse.visibility = View.VISIBLE
                        viewModelLocation.setCurrentLocationData(it)
                    }
                }

            }
        }

        // observe response search location
        lifecycleScope.launch(excHandler) {
            viewModelWeather.flowDataLocation.collectLatest {
                binding.locationLayout.apply {
                    root.visibility = View.VISIBLE
                    cityCountryInfo.text = it.location.locationField

                    binding.locationLayout.statusUse.visibility =
                        if (viewModelLocation.getLastCurrentLocation().name == it.location.name) View.VISIBLE
                        else View.INVISIBLE

                    // set this location
                    root.setOnClickListener { _ ->
                        statusUse.visibility = View.VISIBLE
                        viewModelLocation.setCurrentLocationData(it.location)
                    }
                }
            }
        }

        // search location by text input
        binding.searchButton.setOnClickListener {
            viewModelWeather.getLocationData(
                location = binding.locationInputField.editText?.text.toString()
            )
        }

        // enable/disable button search by textField consists
        binding.locationInputField.editText?.addTextChangedListener {
            binding.searchButton.isEnabled = !(it.isNullOrEmpty())
        }

        binding.currentLocation.setOnClickListener {
            try {
                viewModelLocation.checkLocationPermission()
            } catch (e: Exception) {
                Snackbar.make(binding.root, "Error to get location", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

}