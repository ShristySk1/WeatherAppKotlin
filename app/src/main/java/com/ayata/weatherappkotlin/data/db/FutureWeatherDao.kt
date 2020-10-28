package com.ayata.weatherappkotlin.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ayata.weatherappkotlin.data.entity.FutureWeatherEntry
import com.ayata.weatherappkotlin.data.unitlocalized.future.ImperialSimpleFutureWeatherEntry
import com.ayata.weatherappkotlin.data.unitlocalized.future.MetricSimpleFutureWeatherEntry
import org.threeten.bp.LocalDate
@Dao
interface FutureWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertFutureWeatherEntries(entries: List<FutureWeatherEntry>)

    @Query("SELECT * FROM futureweather_table WHERE date(date) >= date(:startDate)")
     fun getSimpleWeatherForcastMetric(startDate: LocalDate): LiveData<MetricSimpleFutureWeatherEntry>

    @Query("SELECT * FROM futureweather_table WHERE date(date) >= date(:startDate)")
     fun getSimpleWeatherForcastImperial(startDate: LocalDate): LiveData<ImperialSimpleFutureWeatherEntry>

    @Query("SELECT COUNT(id) FROM futureweather_table WHERE date(date) >= date(:startDate)")
    suspend fun countFutureWeather(startDate: LocalDate): Int

    @Query("DELETE FROM futureweather_table WHERE date(date) < date(:startDate)")
    suspend fun deleteOldEntries(startDate: LocalDate)
}