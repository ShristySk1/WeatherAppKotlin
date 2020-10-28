package com.ayata.weatherappkotlin.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ayata.weatherappkotlin.data.network.response.CurrentWeatherResponse
import com.ayata.weatherappkotlin.data.network.response.FutureWeatherResponse
import com.ayata.weatherappkotlin.internal.NoConnectivityException
const val FORCAST_DAYS_COUNT=7
class WeatherNetworkDatasourceImpl(private val apiService: ApiService) : WeatherNetworkDatasource {
    private val _downloadCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    private val _downloadFutureWeather = MutableLiveData<FutureWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadCurrentWeather

    override suspend fun fetchCurrentWeather(location: String) {
        try {
            val fetchCurrentWeather = apiService.getCurrentWeather(location)
            _downloadCurrentWeather.postValue(fetchCurrentWeather)

        } catch (e: Exception) {
            when (e) {
                is NoConnectivityException -> Log.d("Connectivity", "No Connectivity.", e)
                else -> e.stackTrace
            }

        }
    }

    override val downloadFutureWeather: LiveData<FutureWeatherResponse>
        get() = _downloadFutureWeather

    override suspend fun fetchFutureWeather(location: String) {
        try {

            val fetchFutureWeather = apiService.getFutureWeather(location, FORCAST_DAYS_COUNT)
            _downloadFutureWeather.postValue(fetchFutureWeather)

        } catch (e: Exception) {
            when (e) {
                is NoConnectivityException -> Log.d("Connectivity", "No Connectivity.", e)
                else -> e.stackTrace
            }

        }
    }
}