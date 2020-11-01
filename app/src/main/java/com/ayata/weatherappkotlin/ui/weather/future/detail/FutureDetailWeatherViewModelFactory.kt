package com.ayata.weatherappkotlin.ui.weather.future.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ayata.weatherappkotlin.data.provider.UnitProvider
import com.ayata.weatherappkotlin.data.repository.ForcastRepository
import org.threeten.bp.LocalDate

class FutureDetailWeatherViewModelFactory(
    private val detailDate: LocalDate,
    private val repository: ForcastRepository,
    private val unitProvider: UnitProvider
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FutureDetailWeatherViewModel(detailDate,repository, unitProvider) as T
    }
}