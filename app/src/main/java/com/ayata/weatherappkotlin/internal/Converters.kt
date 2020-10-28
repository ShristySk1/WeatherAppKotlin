package com.ayata.weatherappkotlin.internal

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun getStringFromList(list: List<String>?): String {
        return Gson().toJson(list)
    }
    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<String>::class.java).toList()
}