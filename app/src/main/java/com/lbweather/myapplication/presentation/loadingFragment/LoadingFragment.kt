package com.lbweather.myapplication.presentation.loadingFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.lbweather.myapplication.MyApp.Companion.logData
import com.lbweather.myapplication.R
import com.lbweather.myapplication.databinding.FragmentLoadingBinding
import com.lbweather.myapplication.presentation.viewmodel.ViewModelLocation
import com.lbweather.myapplication.presentation.viewmodel.ViewModelWeather
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoadingFragment : Fragment() {

    private lateinit var binding: FragmentLoadingBinding
    private val viewModelWeather: ViewModelWeather by viewModel(ownerProducer = { requireActivity() })

    private val excHandler = CoroutineExceptionHandler { _, throwable ->
        logData("Coroutine Exception. LoadingFragment ($throwable)")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLoadingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch(excHandler) {
            viewModelWeather.flowDataWeather.collectLatest {
                delay(1000L) // optional to show animation more time
                findNavController().navigate(R.id.action_loadingFragment_to_displayWeather)
            }
        }


    }

}