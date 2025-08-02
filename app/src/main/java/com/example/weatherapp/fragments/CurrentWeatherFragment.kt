package com.example.weatherapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.R
import com.example.weatherapp.api.OpenWeatherService
import com.example.weatherapp.api.RetrofitClient
import com.example.weatherapp.database.WeatherDatabase
import com.example.weatherapp.models.CurrentWeather
import com.example.weatherapp.models.WeatherViewModel
//import com.example.weatherapp.utils.WeatherIconMapper
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CurrentWeatherFragment : Fragment() {
    private val viewModel: WeatherViewModel by activityViewModels()

    private lateinit var tvCity: TextView
    private lateinit var tvTemperature: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvHumidity: TextView
    private lateinit var tvWind: TextView
    private lateinit var tvLastUpdated: TextView
    private lateinit var ivWeatherIcon: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var etSearchCity: EditText
    private lateinit var btnSearchCity: Button


    private lateinit var weatherService: OpenWeatherService
    private lateinit var weatherDatabase: WeatherDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_current_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        tvCity = view.findViewById(R.id.tvCity)
        tvTemperature = view.findViewById(R.id.tvTemperature)
        tvDescription = view.findViewById(R.id.tvDescription)
        tvHumidity = view.findViewById(R.id.tvHumidity)
        tvWind = view.findViewById(R.id.tvWind)
        tvLastUpdated = view.findViewById(R.id.tvLastUpdated)
        ivWeatherIcon = view.findViewById(R.id.ivWeatherIcon)
        progressBar = view.findViewById(R.id.progressBar)
        etSearchCity = view.findViewById(R.id.etSearchCity)
        btnSearchCity = view.findViewById(R.id.btnSearchCity)


        // Initialize API service and database
        weatherService = RetrofitClient.weatherService
        weatherDatabase = WeatherDatabase.getInstance(requireContext())

        // Search button click
        btnSearchCity.setOnClickListener {
            val city = etSearchCity.text.toString().trim()
            if (city.isNotEmpty()) {
                viewModel.setCity(city)
                loadWeatherData(city)
            } else {
                Toast.makeText(requireContext(), "Please enter a city", Toast.LENGTH_SHORT).show()
            }
        }
        // Load weather data
        loadWeatherData(viewModel.selectedCity.value ?: "Chandigarh")
    }

    private fun loadWeatherData(city: String) {
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                // Fetch fresh data from API
                val response = weatherService.getCurrentWeather(
                    city,
                    "metric",
                    BuildConfig.OPENWEATHER_API_KEY
                )

                if (response.isSuccessful) {
                    val weatherData = response.body()
                    weatherData?.let {
                        val currentWeather = CurrentWeather(
                            id = 1,
                            cityName = it.name,
                            temperature = it.main.temp,
                            description = it.weather[0].description,
                            humidity = it.main.humidity,
                            windSpeed = it.wind.speed,
                            iconCode = it.weather[0].icon,
                            timestamp = System.currentTimeMillis()
                        )

                        // Save to DB
                        withContext(Dispatchers.IO) {
                            weatherDatabase.weatherDao().insertCurrentWeather(currentWeather)
                        }

                        updateUI(currentWeather)
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "City not found or Error: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Failed: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun updateUI(weather: CurrentWeather) {
        tvCity.text = weather.cityName
        tvTemperature.text = "${weather.temperature.toInt()}°C"
        tvDescription.text = weather.description.capitalize(Locale.getDefault())
        tvHumidity.text = "Humidity: ${weather.humidity}%"
        tvWind.text = "Wind: ${weather.windSpeed} m/s"

        val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        val lastUpdated = dateFormat.format(Date(weather.timestamp))
        tvLastUpdated.text = "Last updated: $lastUpdated"

        // Load weather icon
        val iconUrl = "https://openweathermap.org/img/wn/${weather.iconCode}@2x.png"
        Picasso.get().load(iconUrl).into(ivWeatherIcon)
    }
}