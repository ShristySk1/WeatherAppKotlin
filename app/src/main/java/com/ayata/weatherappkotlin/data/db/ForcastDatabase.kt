package com.ayata.weatherappkotlin.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ayata.weatherappkotlin.data.entity.CurrentWeatherEntry
import com.ayata.weatherappkotlin.data.entity.FutureWeatherEntry
import com.ayata.weatherappkotlin.data.entity.WeatherLocation
import com.ayata.weatherappkotlin.internal.Converters

@Database(
    entities = [CurrentWeatherEntry::class,FutureWeatherEntry::class, WeatherLocation::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(LocalDateConverter::class)
abstract class ForcastDatabase : RoomDatabase() {

    abstract fun getForcastDao(): CurrentWeatherDao
    abstract fun getWeatherLocationDao(): WeatherLocationDao
    abstract fun getFutureWeatherDao(): FutureWeatherDao

    companion object {
        //thread will have immediate aceess to this database
        @Volatile
        private var instance: ForcastDatabase? = null
        private val LOCK = Any()
        operator fun invoke(context: Context) =
            instance ?: synchronized(LOCK) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }


        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ForcastDatabase::class.java,
                "forcastweather_db.db"
            ).build()
    }
}