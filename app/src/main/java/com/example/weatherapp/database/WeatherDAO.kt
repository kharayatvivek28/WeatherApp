package com.example.weatherapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.models.CurrentWeather
import com.example.weatherapp.models.ForecastItem

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(currentWeather: CurrentWeather)

    @Query("SELECT * FROM current_weather LIMIT 1")
    suspend fun getCurrentWeather(): CurrentWeather?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(forecastItems: List<ForecastItem>)

    @Query("SELECT * FROM forecast ORDER BY date ASC")
    suspend fun getForecast(): List<ForecastItem>

    @Query("DELETE FROM forecast")
    suspend fun clearForecast()
}