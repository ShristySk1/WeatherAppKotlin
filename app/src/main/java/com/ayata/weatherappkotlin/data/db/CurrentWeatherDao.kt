package com.ayata.weatherappkotlin.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ayata.weatherappkotlin.data.entity.CURRENT_WEATHER_ID
import com.ayata.weatherappkotlin.data.entity.CurrentWeatherEntry
import com.ayata.weatherappkotlin.data.unitlocalized.current.ImperialCurrentWeatherEntry
import com.ayata.weatherappkotlin.data.unitlocalized.current.MetricCurrentWeatherEntry


@Dao
interface CurrentWeatherDao {
    @Insert(onConflict =OnConflictStrategy.REPLACE)
    suspend fun upsert(weatherEntry: CurrentWeatherEntry)

    @Query("SELECT * from currentweather_table WHERE id=$CURRENT_WEATHER_ID")
     fun getImperialCurrentWeather():LiveData<ImperialCurrentWeatherEntry>


    @Query("SELECT * from currentweather_table WHERE id=$CURRENT_WEATHER_ID")
    fun getMetricCurrentWeather():LiveData<MetricCurrentWeatherEntry>
}