package com.ayata.weatherappkotlin.data.db

import androidx.room.TypeConverter
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

object LocalDateConverter {
    @TypeConverter
    @JvmStatic
    fun stringToDate(date: String?) = date?.let { date ->
        LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
    }

    @TypeConverter
    @JvmStatic
    fun dateToString(date: LocalDate?) = date?.let { date ->
        date.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }
}