package com.example.weatherapp.models

data class ForecastResponse(
    val list: List<ForecastListItem>
)

data class ForecastListItem(
    val dt: Long, // Date time in seconds
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind
)