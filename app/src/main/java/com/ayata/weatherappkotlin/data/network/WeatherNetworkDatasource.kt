package com.ayata.weatherappkotlin.data.network

import androidx.lifecycle.LiveData
import com.ayata.weatherappkotlin.data.network.response.CurrentWeatherResponse
import com.ayata.weatherappkotlin.data.network.response.FutureWeatherResponse

interface WeatherNetworkDatasource {
    val downloadedCurrentWeather:LiveData<CurrentWeatherResponse>
    suspend fun fetchCurrentWeather(location:String)

    val downloadFutureWeather:LiveData<FutureWeatherResponse>
    suspend fun fetchFutureWeather(location: String)
}