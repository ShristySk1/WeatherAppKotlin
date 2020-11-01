package com.ayata.weatherappkotlin.data.unitlocalized.future.detail

import org.threeten.bp.LocalDate


interface UnitSpecificDetailFutureWeatherEntry {
    val date: LocalDate
    val mavTemperature:Double
    val minTemperature:Double
    val avgTemperature: Double
    val conditionText: String
    val conditionIconUrl:String
    val totalPrecicpitation:Double
    val avgVisibilityDistance:Double

}