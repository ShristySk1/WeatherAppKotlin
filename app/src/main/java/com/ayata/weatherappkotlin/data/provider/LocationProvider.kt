package com.ayata.weatherappkotlin.data.provider

import com.ayata.weatherappkotlin.data.entity.WeatherLocation

interface LocationProvider {
    suspend fun hasLocationChanged(lastWeatherLocation:WeatherLocation):Boolean
    suspend fun getPreferredLocationString():String
}