package com.lbweather.getweatherfromall.presentation.parentDisplayFragment

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lbweather.getweatherfromall.presentation.parentDisplayFragment.displayFragment.DisplayWeather
import com.lbweather.getweatherfromall.presentation.parentDisplayFragment.futureForecastFragment.FutureForecastFragment

class CustomPagerAdapter(fragmentActivity: Fragment) :
    FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf(
        DisplayWeather(),
        FutureForecastFragment(),
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}