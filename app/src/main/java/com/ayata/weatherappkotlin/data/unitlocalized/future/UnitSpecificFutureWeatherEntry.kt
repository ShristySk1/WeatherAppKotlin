package com.ayata.weatherappkotlin.data.unitlocalized.future

import org.threeten.bp.LocalDate


interface UnitSpecificFutureWeatherEntry {
    val date: LocalDate
    val avgTemperature: Double
    val conditionText: String
    val conditionIconUrl:String

}