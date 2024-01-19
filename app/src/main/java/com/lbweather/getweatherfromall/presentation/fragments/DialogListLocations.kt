package com.lbweather.getweatherfromall.presentation.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Rect
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.lbweather.getweatherfromall.MyApp.Companion.logData
import com.lbweather.getweatherfromall.R
import com.lbweather.getweatherfromall.databinding.DialogLocationsBinding
import com.lbweather.getweatherfromall.domain.model.listOfUkraineCity
import com.lbweather.getweatherfromall.domain.model.weather.WeatherDataModel
import com.lbweather.getweatherfromall.domain.usecase.GoogleAdsUseCase
import com.lbweather.getweatherfromall.domain.usecase.GoogleAdsUseCase.Companion.ID_LOCATION_SEARCH_BANNER
import com.lbweather.getweatherfromall.domain.usecase.SetLocationByGPS
import com.lbweather.getweatherfromall.presentation.DialogLocationPermission.dialogGPS
import com.lbweather.getweatherfromall.presentation.DialogLocationPermission.dialogPermission
import com.lbweather.getweatherfromall.presentation.viewmodel.LocationViewModel
import com.lbweather.getweatherfromall.presentation.viewmodel.PreferenceViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.util.Locale

class DialogListLocations : DialogFragment(), SetLocationByGPS {

    private lateinit var binding: DialogLocationsBinding

    private val locationViewModel: LocationViewModel by activityViewModel()

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private val enabled by lazy {
        requireContext().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
    }

    private lateinit var permissionRequestLauncher: ActivityResultLauncher<String>

    private val excHandler = CoroutineExceptionHandler { _, throwable ->
        logData("Coroutine Exception. DialogListLocations ($throwable)")
    }

    private var adView: AdView? = null
    private var initialLayoutComplete = false
    private val adSize: AdSize
        get() {
            val bounds: Rect = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                requireActivity().windowManager.currentWindowMetrics.bounds
            } else {
                val displayMetrics = DisplayMetrics()
                @Suppress("DEPRECATION")
                requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
                Rect(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
            }

            var adWidthPixels = binding.adsBannerBox.width.toFloat()

            if (adWidthPixels == 0f) {
                adWidthPixels = bounds.width().toFloat()
            }

            val density = resources.displayMetrics.density
            val adWidth = (adWidthPixels / density).toInt()

            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                requireContext(),
                adWidth
            )
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

        adView = AdView(requireContext())
        binding.adsBannerBox.addView(adView)

        binding.adsBannerBox.viewTreeObserver.addOnGlobalLayoutListener {
            if (!initialLayoutComplete) {
                initialLayoutComplete = true
                loadBanner()
            }
        }

        permissionRequestLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { permission ->
            if (permission) {
                logData("permission is success")

                if (!isActiveGPS()) {
                    logData("gps is not active")
                    requireContext().dialogGPS.show()
                } else {
                    logData("gps is active")
                    getLastLocation()
                }

            } else {
                logData("permission is not success")
                requireContext().dialogPermission.show()
            }

        }

        // observe response search location
        lifecycleScope.launch(excHandler) {
            locationViewModel.searchLocationFlow.collectLatest { locationSearch ->
                binding.locationLayout.apply {
                    root.visibility = View.VISIBLE
                    locationSearch.apply {
                        cityCountryInfo.text = resources.getString(
                            R.string.location_field_tt,
                            shortLocation,
                            if (shortLocation in listOfUkraineCity) resources.getString(R.string.ukraine_country) else country
                        )
                    }

                    val currentActiveLocation = withContext(Dispatchers.IO + excHandler) {
                        locationViewModel.getActiveLocation()
                    }

                    binding.locationLayout.statusUse.visibility =
                        if (currentActiveLocation.region == locationSearch.region) {
                            View.VISIBLE
                        } else View.INVISIBLE

                    // set this location
                    root.setOnClickListener { _ ->
                        statusUse.visibility = View.VISIBLE
                        locationViewModel.updateActiveLocation(locationSearch)
                        locationViewModel.insertNewLocationIfNotExists(locationSearch)
                    }
                }
            }
        }

        // search location by text input
        binding.searchButton.setOnClickListener {
            locationViewModel.searchLocation(
                location = binding.locationInputField.editText?.text.toString(),
                lang = Locale.getDefault().language
            )
        }


        binding.currentLocation.setOnClickListener {
            try {
                checkPermissionLocation()
            } catch (e: Exception) {
                logData(e)
                Snackbar.make(
                    binding.root,
                    resources.getString(R.string.no_location_permission),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun setGSPLocation(locationName: String?) {
        locationName?.let {
            locationViewModel.searchLocation(
                location = it,
                lang = Locale.getDefault().language
            )
        }

    }

    private fun checkPermissionLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (isActiveGPS()) {
                logData("gps is active")
                getLastLocation()
            } else {
                logData("gps is not active")
                requireContext().dialogGPS.show()
            }

        } else {
            permissionRequestLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    private fun isActiveGPS(): Boolean {
        return enabled.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                logData("location is $location")

                val geocoder = Geocoder(requireContext(), Locale.UK)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(location.latitude, location.longitude, 1) {
                        logData(it[0].locality)
                        setGSPLocation(it[0].locality)
                    }
                } else {
                    @Suppress("DEPRECATION")
                    geocoder.getFromLocation(location.latitude, location.longitude, 1)?.let {
                        logData(it[0].locality)
                        setGSPLocation(it[0].locality)
                    }
                }

            } else {
                logData("location is null")
                setGSPLocation(null)
            }
        }.addOnFailureListener {
            setGSPLocation(null)
            logData("error getting location")
        }
    }

    private fun loadBanner() {
        adView?.let { adView ->
            adView.adUnitId = ID_LOCATION_SEARCH_BANNER
            adView.setAdSize(adSize)
            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)
        }

    }

}