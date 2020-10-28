package com.ayata.weatherappkotlin.ui.weather.current

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayata.weatherappkotlin.data.provider.UnitProvider
import com.ayata.weatherappkotlin.data.repository.ForcastRepository
import com.ayata.weatherappkotlin.data.unitlocalized.current.UnitSpecificCurrentWeatherEntry

import com.ayata.weatherappkotlin.internal.UnitSystem
import kotlinx.coroutines.async


class CurrentWeatherViewModel(val repository: ForcastRepository,unitProvider: UnitProvider) : ViewModel() {
    val currentWeatherData: MutableLiveData<UnitSpecificCurrentWeatherEntry> = MutableLiveData()

    private val unitSystem = unitProvider.getUnitSystem()
    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    init {
//        getWeather()
        Log.d("fetchlaunch", ": ");
    }

    val weather by lazy {
        viewModelScope.async {
            Log.d("fetchviewmodel)", ": ");
            repository.getCurrentWeather(isMetric)
        }
    }
    val weatherLocation by lazy{
        viewModelScope.async {
            repository.getWeatherLocation()
        }
    }
}
//    fun getWeather() =
//        viewModelScope.launch {
//            Log.d("fetchlaunch", ": start");
//            val res = repository.getCurrentWeather(isSpeed).value;
//            currentWeatherData.postValue(res)
//            Log.d("fetchlaunch", ": " + currentWeatherData.value?.temperature);
//
//            Log.d("fetchlaunch", ": end");
//
//        }


