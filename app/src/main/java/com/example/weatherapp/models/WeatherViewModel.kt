package com.example.weatherapp.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WeatherViewModel : ViewModel() {
    private val _selectedCity = MutableLiveData<String>("Chandigarh") // Default city
    val selectedCity: LiveData<String> = _selectedCity

    private val _unitPreference = MutableLiveData<String>("metric")
    val unitPreference: LiveData<String> = _unitPreference

    fun setCity(city: String) {
        _selectedCity.value = city
    }
    fun setUnit(unit: String) {
        _unitPreference.value = unit
    }
}