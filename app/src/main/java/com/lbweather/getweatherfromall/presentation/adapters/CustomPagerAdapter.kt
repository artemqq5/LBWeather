package com.lbweather.getweatherfromall.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lbweather.getweatherfromall.presentation.fragments.DisplayWeather
import com.lbweather.getweatherfromall.presentation.fragments.FutureForecastFragment

class CustomPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val fragments = listOf(
        DisplayWeather(),
        FutureForecastFragment(),
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}