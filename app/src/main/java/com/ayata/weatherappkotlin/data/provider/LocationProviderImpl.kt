package com.ayata.weatherappkotlin.data.provider

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.ayata.weatherappkotlin.data.entity.WeatherLocation
import com.ayata.weatherappkotlin.internal.Constants.Companion.CUSTOM_LOCATION
import com.ayata.weatherappkotlin.internal.Constants.Companion.USE_DEVICE_LOCATION
import com.ayata.weatherappkotlin.internal.LocationPermissionNotGrantedException
import com.ayata.weatherappkotlin.internal.asDeferred
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Deferred


class LocationProviderImpl(
    private var fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context
) : PreferenceProvider(context),LocationProvider {
    override suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        var deviceLlocationChanged = try {
            hasDeviceLocationChanged(lastWeatherLocation)
        } catch (exception: LocationPermissionNotGrantedException) {
            false
        }
        return deviceLlocationChanged || hasCustomLocationChanged(lastWeatherLocation);
    }

    override suspend fun getPreferredLocationString(): String {

        if (isUsingDeviceLocation()) {
            Log.d("deviceLocationtext", "getPreferredLocationString: ");
            try {
                val deviceLocation =
                    getLastDeviceLocation().await() ?: return "${getCustomLocationName()}"
                Log.d(
                    "deviceLocation",
                    "getPreferredLocationString: ${deviceLocation.latitude},${deviceLocation.longitude}"
                )
                return "${deviceLocation.latitude},${deviceLocation.longitude}"
            } catch (exception: LocationPermissionNotGrantedException) {
                Log.d("deviceLocationtext", "getPreferredLocationString: catch");
                return "${getCustomLocationName()}"
            }
        } else {
            Log.d("deviceLocationtext", "not even ot: ");
            return "${getCustomLocationName()}"
        }
    }

    private fun hasCustomLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        if (!isUsingDeviceLocation()) {
            val customLocationName = getCustomLocationName()
            return customLocationName != lastWeatherLocation.name
        }
        return false
    }

    private fun getCustomLocationName(): String? {
        return preferences.getString(CUSTOM_LOCATION, null)
    }

    private suspend fun hasDeviceLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        if (!isUsingDeviceLocation()) {
            return false
        }
        val deviceLocation = getLastDeviceLocation().await() ?: return false
        //comparing doubles cannot be done with ==
        val comparisonThreshold = 0.03
        return Math.abs(deviceLocation.latitude - lastWeatherLocation.lat) > comparisonThreshold &&
                Math.abs(deviceLocation.longitude - lastWeatherLocation.lon) > comparisonThreshold
    }

    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocation(): Deferred<Location?> {
        Log.d("locationfetch", "getLastDeviceLocation: ");
        return if (hasLocationPermission()) fusedLocationProviderClient.lastLocation.asDeferred() else throw LocationPermissionNotGrantedException()
    }

    private fun isUsingDeviceLocation(): Boolean {
        return preferences.getBoolean(USE_DEVICE_LOCATION, true)
    }


    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            appContext,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    }
}