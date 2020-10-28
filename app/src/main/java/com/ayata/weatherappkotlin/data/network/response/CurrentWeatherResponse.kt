package com.ayata.weatherappkotlin.data.network.response
import com.ayata.weatherappkotlin.data.entity.CurrentWeatherEntry
import com.ayata.weatherappkotlin.data.entity.WeatherLocation
import com.google.gson.annotations.SerializedName


data class CurrentWeatherResponse(
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry,
    val location: WeatherLocation
)