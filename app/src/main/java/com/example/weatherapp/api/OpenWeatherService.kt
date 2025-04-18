package com.example.weatherapp.api

import com.example.weatherapp.models.CurrentWeatherResponse
import com.example.weatherapp.models.ForecastResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.weatherapp.BuildConfig

interface OpenWeatherService {

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String = BuildConfig.OPENWEATHER_API_KEY  // Use the API key from BuildConfig
    ): Response<CurrentWeatherResponse>

    @GET("forecast")
    suspend fun getForecast(
        @Query("q") city: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String = BuildConfig.OPENWEATHER_API_KEY  // Use the API key from BuildConfig
    ): Response<ForecastResponse>
}
