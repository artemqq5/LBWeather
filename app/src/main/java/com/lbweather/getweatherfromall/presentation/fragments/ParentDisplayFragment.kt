package com.lbweather.getweatherfromall.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.lbweather.getweatherfromall.R
import com.lbweather.getweatherfromall.databinding.FragmentParentDisplayBinding
import com.lbweather.getweatherfromall.presentation.adapters.CustomPagerAdapter

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

        // Disable swipe
        binding.viewPager.isUserInputEnabled = false

        // Set the adapter
        binding.viewPager.adapter = CustomPagerAdapter(this)

        // Connect the TabLayout and the ViewPager2
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = resources.getString(R.string.current_weather)
                }

                1 -> {
                    tab.text = resources.getString(R.string.for_3_days)
                }
            }
        }.attach()

    }

}

