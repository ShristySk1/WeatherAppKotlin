package com.ayata.weatherappkotlin.data.entity


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "futureweather_table", indices = [Index(value = ["date"], unique = true)])
data class FutureWeatherEntry(
    @PrimaryKey(autoGenerate = true)
    var id: Int?=null,
    @Embedded
    val astro: Astro,
    val date: String,
    @Embedded
    val day: Day,
)