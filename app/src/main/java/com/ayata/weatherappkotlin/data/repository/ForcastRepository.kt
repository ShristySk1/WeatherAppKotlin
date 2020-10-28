package com.ayata.weatherappkotlin.data.repository

import androidx.lifecycle.LiveData
import com.ayata.weatherappkotlin.data.entity.WeatherLocation
import com.ayata.weatherappkotlin.data.unitlocalized.current.UnitSpecificCurrentWeatherEntry

interface ForcastRepository {
    suspend fun getCurrentWeather(isMetric:Boolean):LiveData<out UnitSpecificCurrentWeatherEntry>
    suspend fun getWeatherLocation():LiveData<WeatherLocation>

}