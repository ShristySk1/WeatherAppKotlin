package com.ayata.weatherappkotlin.data.unitlocalized.future.detail

import androidx.room.ColumnInfo
import org.threeten.bp.LocalDate

data class MetricDetailFutureWeatherEntry(
    @ColumnInfo(name = "date")
    override val date: LocalDate,
    @ColumnInfo(name = "maxtempC")
    override val mavTemperature: Double,
    @ColumnInfo(name = "mintempC")
    override val minTemperature: Double,
    @ColumnInfo(name = "avgtempC")
    override val avgTemperature: Double,
    @ColumnInfo(name = "condition_text")
    override val conditionText: String,
    @ColumnInfo(name = "condition_icon")
    override val conditionIconUrl: String,
    @ColumnInfo(name = "totalprecipMm")
    override val totalPrecicpitation: Double,
    @ColumnInfo(name = "avgvisKm")
    override val avgVisibilityDistance: Double
) : UnitSpecificDetailFutureWeatherEntry