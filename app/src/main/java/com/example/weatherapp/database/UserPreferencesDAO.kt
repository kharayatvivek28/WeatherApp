package com.example.weatherapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.models.UserPreferences
//import retrofit2.http.Query

@Dao
interface UserPreferencesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserPreferences(userPreferences: UserPreferences)

    @Query("SELECT * FROM user_preferences LIMIT 1")
    suspend fun getUserPreferences(): UserPreferences?
}