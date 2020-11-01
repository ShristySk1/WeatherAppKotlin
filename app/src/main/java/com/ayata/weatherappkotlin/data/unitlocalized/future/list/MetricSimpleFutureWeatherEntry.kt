package com.ayata.weatherappkotlin.data.unitlocalized.future.list

import androidx.room.ColumnInfo
import org.threeten.bp.LocalDate


data class MetricSimpleFutureWeatherEntry(
    @ColumnInfo(name="condition_text")
    override val conditionText: String,
    @ColumnInfo(name="condition_icon")
    override val conditionIconUrl: String,
    @ColumnInfo(name = "date")
    override val date: LocalDate,
    @ColumnInfo(name = "avgtempC")
    override val avgTemperature: Double
)
    : UnitSpecificFutureWeatherEntry