package com.ayata.weatherappkotlin.data.provider

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.ayata.weatherappkotlin.internal.Constants.Companion.UNIT_SYSTEM
import com.ayata.weatherappkotlin.internal.UnitSystem



class UnitProviderImpl(context: Context) :PreferenceProvider(context), UnitProvider {

    override fun getUnitSystem(): UnitSystem {
        val selectedName = preferences.getString(UNIT_SYSTEM, UnitSystem.METRIC.name)
        return UnitSystem.valueOf(selectedName!!)
    }
}