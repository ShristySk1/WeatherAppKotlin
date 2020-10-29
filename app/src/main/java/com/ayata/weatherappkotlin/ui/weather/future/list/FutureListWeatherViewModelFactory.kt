package com.ayata.weatherappkotlin.ui.weather.future.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ayata.weatherappkotlin.data.provider.UnitProvider
import com.ayata.weatherappkotlin.data.repository.ForcastRepository

class FutureListWeatherViewModelFactory(private val repository: ForcastRepository, private val unitProvider: UnitProvider):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FutureListWeatherViewModel(repository,unitProvider) as T
    }
}