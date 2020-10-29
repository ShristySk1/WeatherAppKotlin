package com.ayata.weatherappkotlin.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayata.weatherappkotlin.data.provider.UnitProvider
import com.ayata.weatherappkotlin.data.repository.ForcastRepository
import com.ayata.weatherappkotlin.internal.UnitSystem
import kotlinx.coroutines.async

abstract class WeatherViewModel(private val repository: ForcastRepository,
                                private val unitProvider: UnitProvider
) : ViewModel() {

    private val unitSystem = unitProvider.getUnitSystem()
    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC
    val weatherLocation by lazy{
        viewModelScope.async {
            repository.getWeatherLocation()
        }
    }
}