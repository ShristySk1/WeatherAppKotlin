package com.ayata.weatherappkotlin.ui.weather.future.list

import androidx.lifecycle.viewModelScope
import com.ayata.weatherappkotlin.data.provider.UnitProvider
import com.ayata.weatherappkotlin.data.repository.ForcastRepository
import com.ayata.weatherappkotlin.ui.base.WeatherViewModel
import kotlinx.coroutines.async
import org.threeten.bp.LocalDate

class FutureListWeatherViewModel(
    private val repository: ForcastRepository,
    private val unitProvider: UnitProvider
) : WeatherViewModel(repository, unitProvider) {

     val futureWeatherEntries = viewModelScope.async {
        repository.getFutureWeatherList(LocalDate.now(), super.isMetric)
    }
}