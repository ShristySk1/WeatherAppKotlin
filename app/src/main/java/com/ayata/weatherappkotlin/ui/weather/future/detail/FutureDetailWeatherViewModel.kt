package com.ayata.weatherappkotlin.ui.weather.future.detail

import com.ayata.weatherappkotlin.data.provider.UnitProvider
import com.ayata.weatherappkotlin.data.repository.ForcastRepository
import com.ayata.weatherappkotlin.ui.base.WeatherViewModel
import org.threeten.bp.LocalDate

class FutureDetailWeatherViewModel(
    private val detailDate:LocalDate,
    private val repository: ForcastRepository,
    private val unitProvider: UnitProvider
) : WeatherViewModel(repository, unitProvider) {

    val futureWeatherDetail by lazy {
        repository.getWeatherDetailByDate(super.isMetric, detailDate)
    }
}