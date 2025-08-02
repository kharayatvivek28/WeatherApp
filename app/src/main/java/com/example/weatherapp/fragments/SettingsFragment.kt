package com.example.weatherapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.R
import com.example.weatherapp.database.WeatherDatabase
import com.example.weatherapp.models.UserPreferences
import com.example.weatherapp.models.WeatherViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsFragment : Fragment() {
    private val viewModel: WeatherViewModel by activityViewModels()

    private lateinit var tvUserEmail: TextView
    private lateinit var rgUnits: RadioGroup
    private lateinit var btnSaveSettings: Button

    private lateinit var weatherDatabase: WeatherDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        tvUserEmail = view.findViewById(R.id.tvUserEmail)
        rgUnits = view.findViewById(R.id.rgUnits)
        btnSaveSettings = view.findViewById(R.id.btnSaveSettings)

        // Initialize Firebase Auth and database
        auth = FirebaseAuth.getInstance()
        weatherDatabase = WeatherDatabase.getInstance(requireContext())

        // Display user email
        tvUserEmail.text = auth.currentUser?.email ?: "Not signed in"

        // Load user preferences
        loadUserPreferences()

        // Set up save button
        btnSaveSettings.setOnClickListener {
            saveUserPreferences()
        }
    }

    private fun loadUserPreferences() {
        lifecycleScope.launch {
            val preferences = withContext(Dispatchers.IO) {
                weatherDatabase.userPreferencesDao().getUserPreferences()
            }

            preferences?.let {
                when (it.units) {
                    "metric" -> rgUnits.check(R.id.rbMetric)
                    "imperial" -> rgUnits.check(R.id.rbImperial)
                }
                viewModel.setUnit(it.units)
            }
        }
    }

    private fun saveUserPreferences() {
        val units = when (rgUnits.checkedRadioButtonId) {
            R.id.rbMetric -> "metric"
            R.id.rbImperial -> "imperial"
            else -> "metric"
        }

        lifecycleScope.launch {
            val preferences = UserPreferences(
                id = 1, // Single row for user preferences
                units = units
            )

            withContext(Dispatchers.IO) {
                weatherDatabase.userPreferencesDao().insertUserPreferences(preferences)
            }
            viewModel.setUnit(units)

            // Show confirmation
            view?.let {
                com.google.android.material.snackbar.Snackbar.make(
                    it,
                    "Settings saved",
                    com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }
}