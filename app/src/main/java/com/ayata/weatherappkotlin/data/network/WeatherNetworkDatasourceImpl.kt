package com.ayata.weatherappkotlin.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ayata.weatherappkotlin.data.network.response.CurrentWeatherResponse
import com.ayata.weatherappkotlin.data.network.response.FutureWeatherResponse
import com.ayata.weatherappkotlin.internal.Constants.Companion.FORCAST_DAYS_COUNT
import com.ayata.weatherappkotlin.internal.NoConnectivityException

class WeatherNetworkDatasourceImpl(private val apiService: ApiService) : WeatherNetworkDatasource {
    private val _downloadCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    private val _downloadFutureWeather = MutableLiveData<FutureWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadCurrentWeather

    override suspend fun fetchCurrentWeather(location: String) {
//        try {

            val fetchCurrentWeather = apiService.getCurrentWeather(location).await()

//            if(fetchCurrentWeatherResponse.isSuccessful){
                Log.d("testresponse", "fetchCurrentWeather: "+fetchCurrentWeather);
                _downloadCurrentWeather.postValue(fetchCurrentWeather)
//            }else{
//                Log.d("testresponse", "not successful: ");
//            }


//        } catch (e: Exception) {
//            when (e) {
//                is NoConnectivityException -> Log.d("Connectivity", "No Connectivity.", e)
//                else -> {
//                    Log.d("testresponse faile", "fetchCurrentWeather: ");
//                    e.stackTrace}
//            }
//
//        }
    }

    override val downloadFutureWeather: LiveData<FutureWeatherResponse>
        get() = _downloadFutureWeather

    override suspend fun fetchFutureWeather(location: String) {
        try {

            val fetchFutureWeather = apiService.getFutureWeather(location, FORCAST_DAYS_COUNT).await()
//            if(fetchFutureWeatherResponse.isSuccessful) {
                _downloadFutureWeather.postValue(fetchFutureWeather)
                Log.d(
                    "fetchdatasize",
                    "fetchFutureWeather: " + fetchFutureWeather.futureWeatherEntries.entries.size
                );
//            }else{
//                Log.d("testresponseFuture", "not successful: ");
//            }

        } catch (e: Exception) {
            when (e) {
                is NoConnectivityException -> Log.d("Connectivity", "No Connectivity.", e)
                else -> e.stackTrace
            }

        }
    }
}