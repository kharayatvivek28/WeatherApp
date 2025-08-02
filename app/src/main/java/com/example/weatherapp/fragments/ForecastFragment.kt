package com.example.weatherapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.R
import com.example.weatherapp.adapters.ForecastAdapter
import com.example.weatherapp.api.OpenWeatherService
import com.example.weatherapp.api.RetrofitClient
import com.example.weatherapp.database.WeatherDatabase
import com.example.weatherapp.models.ForecastItem
import com.example.weatherapp.models.WeatherViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.fragment.app.activityViewModels
import kotlinx.coroutines.withContext

class ForecastFragment : Fragment() {
    private val viewModel: WeatherViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: ForecastAdapter

    private lateinit var weatherService: OpenWeatherService
    private lateinit var weatherDatabase: WeatherDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forecast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        recyclerView = view.findViewById(R.id.recyclerViewForecast)
        progressBar = view.findViewById(R.id.progressBarForecast)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ForecastAdapter()
        recyclerView.adapter = adapter

        // Initialize API service and database
        weatherService = RetrofitClient.weatherService
        weatherDatabase = WeatherDatabase.getInstance(requireContext())

        // Load forecast data
        viewModel.selectedCity.observe(viewLifecycleOwner) { city ->
            loadForecastData(city)
        }
    }

    private fun loadForecastData(city: String) {
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                // Try to get cached data first
                val cachedForecast = withContext(Dispatchers.IO) {
                    weatherDatabase.weatherDao().getForecast()
                }

                if (cachedForecast.isNotEmpty()) {
                    adapter.submitList(cachedForecast)
                }

                // Fetch fresh data from API
                val response = weatherService.getForecast(
                    city,
                    "metric", // Units
                    BuildConfig.OPENWEATHER_API_KEY
                )

                if (response.isSuccessful) {
                    val forecastData = response.body()
                    forecastData?.let { data ->
                        val forecastItems = data.list.map { item ->
                            ForecastItem(
                                id = 0, // Auto-generated
                                date = item.dt * 1000, // Convert to milliseconds
                                temperature = item.main.temp,
                                description = item.weather[0].description,
                                iconCode = item.weather[0].icon,
                                humidity = item.main.humidity,
                                windSpeed = item.wind.speed
                            )
                        }

                        // Save to database
                        withContext(Dispatchers.IO) {
                            weatherDatabase.weatherDao().clearForecast()
                            weatherDatabase.weatherDao().insertForecast(forecastItems)
                        }

                        // Update UI
                        adapter.submitList(forecastItems)
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Error: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
}