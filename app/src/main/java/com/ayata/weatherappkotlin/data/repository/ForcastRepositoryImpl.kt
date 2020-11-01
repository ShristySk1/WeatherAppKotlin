package com.ayata.weatherappkotlin.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.ayata.weatherappkotlin.data.db.CurrentWeatherDao
import com.ayata.weatherappkotlin.data.db.FutureWeatherDao
import com.ayata.weatherappkotlin.data.db.WeatherLocationDao
import com.ayata.weatherappkotlin.data.entity.WeatherLocation
import com.ayata.weatherappkotlin.data.network.WeatherNetworkDatasource
import com.ayata.weatherappkotlin.data.network.response.CurrentWeatherResponse
import com.ayata.weatherappkotlin.data.network.response.FutureWeatherResponse
import com.ayata.weatherappkotlin.data.provider.LocationProvider
import com.ayata.weatherappkotlin.data.unitlocalized.current.UnitSpecificCurrentWeatherEntry
import com.ayata.weatherappkotlin.data.unitlocalized.future.detail.UnitSpecificDetailFutureWeatherEntry
import com.ayata.weatherappkotlin.data.unitlocalized.future.list.UnitSpecificFutureWeatherEntry
import com.ayata.weatherappkotlin.internal.Constants.Companion.FORCAST_DAYS_COUNT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime

class ForcastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherLocationDao: WeatherLocationDao,
    private val futureWeatherDao: FutureWeatherDao,
    private val weatherNetworkDatasource: WeatherNetworkDatasource,
    private var locationProvider: LocationProvider
) : ForcastRepository {
    init {
        weatherNetworkDatasource.apply {
            downloadedCurrentWeather.observeForever { currentWeatherResponse ->
                Log.d("fetchobserve", ": ");
                GlobalScope.launch(Dispatchers.IO) {
                    persistFetchCurrentWeather(currentWeatherResponse)
                    Log.d("fetchobserve(3)", ": ");
                }
                Log.d("fetchobserve(2)", ": ");
            }
            downloadFutureWeather.observeForever { futureWeatherResponse ->

                GlobalScope.launch(Dispatchers.IO) {
                    persistFetchFutureWeather(futureWeatherResponse)

                }
            }
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

    override suspend fun getFutureWeatherList(
        startDate: LocalDate,
        metric: Boolean
    ): LiveData<out List<UnitSpecificFutureWeatherEntry>> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            if (metric) futureWeatherDao.getSimpleWeatherForcastMetric(startDate) else futureWeatherDao.getSimpleWeatherForcastImperial(
                startDate
            )
        }
    }

    override fun getWeatherDetailByDate(
        isMetric: Boolean,
        date: LocalDate
    ): LiveData<out UnitSpecificDetailFutureWeatherEntry> {
        return if (isMetric) futureWeatherDao.getDetailWeatherByDateMetric(date) else futureWeatherDao.getDetailWeatherByDateImperial(
            date = date
        )
    }

    private suspend fun persistFetchCurrentWeather(fetchWeather: CurrentWeatherResponse) {
        Log.d("fetchmeinsert", "getCurrentWeather: ");
        currentWeatherDao.upsert(fetchWeather.currentWeatherEntry)
        weatherLocationDao.upsert(fetchWeather.location)

    }

    private suspend fun persistFetchFutureWeather(futureWeatherResponse: FutureWeatherResponse) {
        suspend fun deleteOldForcastData() {
            val today = LocalDate.now()
            futureWeatherDao.deleteOldEntries(today)
        }
        deleteOldForcastData()
        futureWeatherDao.upsertFutureWeatherEntries(futureWeatherResponse.futureWeatherEntries.entries)
        weatherLocationDao.upsert(futureWeatherResponse.location)
    }


    private suspend fun initWeatherData() {
        val lastWeatherLocation = weatherLocationDao.getWeatherLocationNonLive()
        if (lastWeatherLocation == null || locationProvider.hasLocationChanged(lastWeatherLocation)) {
//        if (lastWeatherLocation == null) {
            fetchCurrentWeather()
            fetchFutureWeather()
            return
        }
        if (isFetchCurrentNeeded(lastWeatherLocation.zonedDateTime)) {
//        if (isFetchCurrentNeeded(ZonedDateTime.now().minusHours(1))) {
            Log.d("fetchmeinit", "initWeatherData: ");
            fetchCurrentWeather()
        }
        if (isFetchFutureNeeded()) {
            fetchFutureWeather()
        }
    }

    private suspend fun fetchFutureWeather() {
        weatherNetworkDatasource.fetchFutureWeather(locationProvider.getPreferredLocationString())
//        weatherNetworkDatasource.fetchFutureWeather("Kathmandu")
    }

    private suspend fun isFetchFutureNeeded(): Boolean {
        val today = LocalDate.now()
        val futureWeatherCount = futureWeatherDao.countFutureWeather(today)
        return futureWeatherCount < FORCAST_DAYS_COUNT

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