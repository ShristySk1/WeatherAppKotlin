package com.ayata.weatherappkotlin.data.unitlocalized.current


interface UnitSpecificCurrentWeatherEntry {
    val temperature: Double
    val conditionText: String
    val conditionIconUrl: String
    val windDirection: String
    val precipitationVolume: Double
    val feelsLikeTemperature: Double
    val visibilityDistance: Double
}