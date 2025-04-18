package com.example.weatherapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.weatherapp.fragments.CurrentWeatherFragment
import com.example.weatherapp.fragments.ForecastFragment
import com.example.weatherapp.fragments.SettingsFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CurrentWeatherFragment()
            1 -> ForecastFragment()
            2 -> SettingsFragment()
            else -> CurrentWeatherFragment()
        }
    }
}
