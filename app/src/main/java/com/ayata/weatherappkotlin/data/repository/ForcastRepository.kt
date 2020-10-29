package com.ayata.weatherappkotlin.data.repository

import androidx.lifecycle.LiveData
import com.ayata.weatherappkotlin.data.entity.WeatherLocation
import com.ayata.weatherappkotlin.data.unitlocalized.current.UnitSpecificCurrentWeatherEntry
import com.ayata.weatherappkotlin.data.unitlocalized.future.UnitSpecificFutureWeatherEntry
import org.threeten.bp.LocalDate

interface ForcastRepository {
    suspend fun getCurrentWeather(isMetric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry>
    suspend fun getWeatherLocation(): LiveData<WeatherLocation>
    suspend fun getFutureWeatherList(startDate:LocalDate,metric:Boolean): LiveData<out List<UnitSpecificFutureWeatherEntry>>
}