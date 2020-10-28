package com.ayata.weatherappkotlin.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ayata.weatherappkotlin.data.entity.CURRENT_WEATHER_ID
import com.ayata.weatherappkotlin.data.entity.CurrentWeatherEntry
import com.ayata.weatherappkotlin.data.entity.WEATHER_LOCATION_ID
import com.ayata.weatherappkotlin.data.entity.WeatherLocation


@Dao
interface WeatherLocationDao {
    @Insert(onConflict =OnConflictStrategy.REPLACE)
    suspend fun upsert(weatherLocation: WeatherLocation)

    @Query("SELECT * FROM weather_location WHERE id=$WEATHER_LOCATION_ID")
     fun getWeatherLocation():LiveData<WeatherLocation>

    @Query("SELECT * FROM weather_location WHERE id=$WEATHER_LOCATION_ID")
   suspend fun getWeatherLocationNonLive():WeatherLocation

}