package com.ayata.weatherappkotlin.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.ayata.weatherappkotlin.data.db.CurrentWeatherDao
import com.ayata.weatherappkotlin.data.db.WeatherLocationDao
import com.ayata.weatherappkotlin.data.entity.WeatherLocation
import com.ayata.weatherappkotlin.data.network.WeatherNetworkDatasource
import com.ayata.weatherappkotlin.data.network.response.CurrentWeatherResponse
import com.ayata.weatherappkotlin.data.provider.LocationProvider
import com.ayata.weatherappkotlin.data.unitlocalized.current.UnitSpecificCurrentWeatherEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class ForcastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherLocationDao: WeatherLocationDao,
    private val weatherNetworkDatasource: WeatherNetworkDatasource,
    private var locationProvider: LocationProvider
) : ForcastRepository {
    init {
        weatherNetworkDatasource.downloadedCurrentWeather.observeForever { currentWeatherResponse ->
            Log.d("fetchobserve", ": ");
            GlobalScope.launch(Dispatchers.IO) {
                persistFetchCurrentWeather(currentWeatherResponse)
                Log.d("fetchobserve(3)", ": ");
            }
            Log.d("fetchobserve(2)", ": ");
        }
    }

    override suspend fun getCurrentWeather(isMetric: Boolean): LiveData<out UnitSpecificCurrentWeatherEntry> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            Log.d("fetchmeinitcomplete", "getCurrentWeather: ");
            if (isMetric) currentWeatherDao.getMetricCurrentWeather() else currentWeatherDao.getImperialCurrentWeather()
        }
    }

    override suspend fun getWeatherLocation(): LiveData<WeatherLocation> {
        return weatherLocationDao.getWeatherLocation()
    }

    private suspend fun persistFetchCurrentWeather(fetchWeather: CurrentWeatherResponse) {
        Log.d("fetchmeinsert", "getCurrentWeather: ");
        currentWeatherDao.upsert(fetchWeather.currentWeatherEntry)
        weatherLocationDao.upsert(fetchWeather.location)

    }

    private suspend fun initWeatherData() {
        val lastWeatherLocation=weatherLocationDao.getWeatherLocationNonLive()
        if(lastWeatherLocation==null||locationProvider.hasLocationChanged(lastWeatherLocation)) {
            fetchCurrentWeather()
            return
        }
        if (isFetchCurrentNeeded(lastWeatherLocation.zonedDateTime)){
//        if (isFetchCurrentNeeded(ZonedDateTime.now().minusHours(1))) {
            Log.d("fetchmeinit", "initWeatherData: ");
            fetchCurrentWeather()
        }
    }

    private suspend fun fetchCurrentWeather() {
        Log.d("fetchmeactual", "getCurrentWeather: ");
        weatherNetworkDatasource.fetchCurrentWeather(locationProvider.getPreferredLocationString())
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinAgo)
    }
}