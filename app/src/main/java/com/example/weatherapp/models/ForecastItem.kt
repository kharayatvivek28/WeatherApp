package com.example.weatherapp.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "forecast",indices = [Index(value = ["date"])])
data class ForecastItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: Long,
    val temperature: Double,
    val description: String,
    val iconCode: String,
    val humidity: Int,
    val windSpeed: Double
)