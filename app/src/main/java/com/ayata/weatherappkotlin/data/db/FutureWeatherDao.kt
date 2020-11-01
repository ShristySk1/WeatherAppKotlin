package com.ayata.weatherappkotlin.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ayata.weatherappkotlin.data.entity.FutureWeatherEntry
import com.ayata.weatherappkotlin.data.unitlocalized.future.detail.ImperialDetailFutureWeatherEntry
import com.ayata.weatherappkotlin.data.unitlocalized.future.detail.MetricDetailFutureWeatherEntry
import com.ayata.weatherappkotlin.data.unitlocalized.future.list.ImperialSimpleFutureWeatherEntry
import com.ayata.weatherappkotlin.data.unitlocalized.future.list.MetricSimpleFutureWeatherEntry

import org.threeten.bp.LocalDate

@Dao
interface FutureWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertFutureWeatherEntries(entries: List<FutureWeatherEntry>)

    @Query("SELECT * FROM futureweather_table WHERE date(date) >= date(:startDate)")
    fun getSimpleWeatherForcastMetric(startDate: LocalDate): LiveData<List<MetricSimpleFutureWeatherEntry>>

    @Query("SELECT * FROM futureweather_table WHERE date(date) >= date(:startDate)")
    fun getSimpleWeatherForcastImperial(startDate: LocalDate): LiveData<List<ImperialSimpleFutureWeatherEntry>>

    @Query("SELECT COUNT(id) FROM futureweather_table WHERE date(date) >= date(:startDate)")
    suspend fun countFutureWeather(startDate: LocalDate): Int

    @Query("DELETE FROM futureweather_table WHERE date(date) < date(:startDate)")
    suspend fun deleteOldEntries(startDate: LocalDate)

    @Query("SELECT * FROM futureweather_table WHERE date(date) = date(:date)")
    fun getDetailWeatherByDateMetric(date: LocalDate):LiveData<MetricDetailFutureWeatherEntry>

    @Query("SELECT * FROM futureweather_table WHERE date(date) = date(:date)")
    fun getDetailWeatherByDateImperial(date: LocalDate):LiveData<ImperialDetailFutureWeatherEntry>
}