package com.example.weatherapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_preferences")
data class UserPreferences(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Let Room auto-generate the ID (default value is 0)
    val units: String = "metric" // Default to metric
)