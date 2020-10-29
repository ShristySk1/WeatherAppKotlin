package com.ayata.weatherappkotlin

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import com.ayata.weatherappkotlin.data.db.ForcastDatabase
import com.ayata.weatherappkotlin.data.network.*
import com.ayata.weatherappkotlin.data.provider.LocationProvider
import com.ayata.weatherappkotlin.data.provider.LocationProviderImpl
import com.ayata.weatherappkotlin.data.provider.UnitProvider
import com.ayata.weatherappkotlin.data.provider.UnitProviderImpl
import com.ayata.weatherappkotlin.data.repository.ForcastRepository
import com.ayata.weatherappkotlin.data.repository.ForcastRepositoryImpl
import com.ayata.weatherappkotlin.ui.weather.current.CurrentWeatherViewModelFactory
import com.ayata.weatherappkotlin.ui.weather.future.list.FutureListWeatherViewModelFactory
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class ForcastApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@ForcastApplication))

        bind() from singleton { ForcastDatabase(instance()) }
        bind() from singleton { instance<ForcastDatabase>().getForcastDao() }
        bind() from singleton { instance<ForcastDatabase>().getWeatherLocationDao() }
        bind() from singleton { instance<ForcastDatabase>().getFutureWeatherDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { ApiService(instance()) }
        bind<WeatherNetworkDatasource>() with singleton { WeatherNetworkDatasourceImpl(instance()) }
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(), instance()) }
        bind<ForcastRepository>() with singleton {
            ForcastRepositoryImpl(
                instance(),
                instance(),
                instance(),
                instance(),
                instance()
            )
        }
        bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }
        bind() from provider { CurrentWeatherViewModelFactory(instance(), instance()) }
        bind() from provider { FutureListWeatherViewModelFactory(instance(), instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        //for preference default value when run for first time
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }
}
//instance to context,service
