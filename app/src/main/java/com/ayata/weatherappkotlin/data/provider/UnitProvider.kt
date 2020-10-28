package com.ayata.weatherappkotlin.data.provider

import com.ayata.weatherappkotlin.internal.UnitSystem

interface UnitProvider {
    fun getUnitSystem():UnitSystem
}