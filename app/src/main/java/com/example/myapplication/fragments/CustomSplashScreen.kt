package com.example.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.MainActivity.Companion.main_context
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSplashScreenBinding
import com.example.myapplication.helper.InternetConnection.checkForInternet
import com.example.myapplication.sharedPreferences.WeatherPref.getShPrefWeather
import com.example.myapplication.viewmodel.ViewModelWeather

class CustomSplashScreen : Fragment() {

    private lateinit var binding: FragmentSplashScreenBinding
    private val viewModelWeather: ViewModelWeather by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashScreenBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!checkForInternet(main_context)) {

            if (getShPrefWeather() != null) {
                findNavController().navigate(R.id.action_customSplashScreen_to_nav_display)
            } else {
                view.findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                view.findViewById<TextView>(R.id.internetText).text =
                    "Перевірте підключення до інтернету"
            }

        } else {
            Log.i("tytgyhu3j2", "request API from customSplashScreen")
            viewModelWeather.gg("Полтава", "uk") {
                findNavController().navigate(R.id.action_customSplashScreen_to_nav_display)
            }
        }

    }

}