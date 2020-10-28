package com.ayata.weatherappkotlin.data.entity


import com.google.gson.annotations.SerializedName

data class Condition(
    val icon: String,
    val text: String
)