package com.ayata.weatherappkotlin.data.unitlocalized.future.detail

import androidx.room.ColumnInfo
import org.threeten.bp.LocalDate

data class ImperialDetailFutureWeatherEntry(
    @ColumnInfo(name = "date")
    override val date: LocalDate,
    @ColumnInfo(name = "maxtempF")
    override val mavTemperature: Double,
    @ColumnInfo(name = "mintempF")
    override val minTemperature: Double,
    @ColumnInfo(name = "avgtempF")
    override val avgTemperature: Double,
    @ColumnInfo(name = "condition_text")
    override val conditionText: String,
    @ColumnInfo(name = "condition_icon")
    override val conditionIconUrl: String,
    @ColumnInfo(name = "totalprecipIn")
    override val totalPrecicpitation: Double,
    @ColumnInfo(name = "avgvisMiles")
    override val avgVisibilityDistance: Double
) : UnitSpecificDetailFutureWeatherEntry