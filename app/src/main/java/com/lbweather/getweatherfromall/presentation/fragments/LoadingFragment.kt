package com.lbweather.getweatherfromall.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.lbweather.getweatherfromall.MyApp.Companion.logData
import com.lbweather.getweatherfromall.R
import com.lbweather.getweatherfromall.databinding.FragmentLoadingBinding
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoadingFragment : Fragment() {

    private lateinit var binding: FragmentLoadingBinding

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
            delay(300L)
            findNavController().navigate(R.id.action_loadingFragment_to_parentDisplayFragment)
        }

    }

}