package com.ayata.weatherappkotlin.ui.weather.current

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ayata.weatherappkotlin.data.provider.UnitProvider
import com.ayata.weatherappkotlin.data.repository.ForcastRepository
import com.ayata.weatherappkotlin.data.unitlocalized.current.UnitSpecificCurrentWeatherEntry
import com.ayata.weatherappkotlin.ui.base.WeatherViewModel
import kotlinx.coroutines.async


class CurrentWeatherViewModel(val repository: ForcastRepository, unitProvider: UnitProvider) :
    WeatherViewModel(repository, unitProvider) {
    val currentWeatherData: MutableLiveData<UnitSpecificCurrentWeatherEntry> = MutableLiveData()


//    init {
//        getWeather()
//        Log.d("fetchlaunch", ": ");
//    }

    val weather by lazy {
        viewModelScope.async {
            Log.d("fetchviewmodel)", ": ");
            repository.getCurrentWeather(super.isMetric)
        }
    }

}
//    fun getWeather(){
//        viewModelScope.launch {
//            Log.d("fetchlaunch", ": start");
//            Transformations.map(repository.getCurrentWeather(isMetric)) {
//                currentWeatherData.postValue(it)
//                Log.d("fetchlaunch", ": " + it.temperature);
//                Log.d("fetchlaunch", ": " + currentWeatherData.value?.temperature);
//            }
//            Log.d("fetchlaunch", ": end");
//        }}



