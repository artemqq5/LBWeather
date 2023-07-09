package com.lbweather.getweatherfromall.presentation.parentDisplayFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.tabs.TabLayout
import com.lbweather.getweatherfromall.R
import com.lbweather.getweatherfromall.databinding.FragmentParentDisplayBinding

class ParentDisplayFragment : Fragment() {

    private var _binding: FragmentParentDisplayBinding? = null
    private val binding: FragmentParentDisplayBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentParentDisplayBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.sub_nav_fragment_controller) as NavHostFragment
        val navController = navHostFragment.navController

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab) {
                    binding.tabLayout.getTabAt(0) -> {
                        navController.navigate(R.id.action_futureForecastFragment_to_displayWeather2)
                    }
                    binding.tabLayout.getTabAt(1) -> {
                        navController.navigate(R.id.action_displayWeather2_to_futureForecastFragment)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

    }
}

