package com.ayata.weatherappkotlin.data.network.response


import androidx.room.Entity
import com.ayata.weatherappkotlin.data.entity.FutureWeatherEntry
import com.google.gson.annotations.SerializedName

data class ForecastDaysContainer(
    @SerializedName("forecastday")
    val entries: List<FutureWeatherEntry>
)