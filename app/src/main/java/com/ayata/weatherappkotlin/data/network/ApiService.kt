package com.ayata.weatherappkotlin.data.network

import com.ayata.weatherappkotlin.data.network.response.CurrentWeatherResponse
import com.ayata.weatherappkotlin.data.network.response.FutureWeatherResponse
import com.ayata.weatherappkotlin.internal.Constants.Companion.API_KEY
import com.ayata.weatherappkotlin.internal.Constants.Companion.BASE_URL
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * This is a program for creating ApiService so that it is end point to call our restApi.
 * Our URL=http://api.weatherstack.com/current?access_key=5037d163fc91bd0bc7210d9841862ab9&query=Kathmandu&language=en
 * <b>Updated url</b>
 * https://api.weatherapi.com/v1/current.json?key=606f7544be944ccd87f75930200106&q=London
 * getFutureWeatherURL=http://api.weatherapi.com/v1/forecast.json?key=606f7544be944ccd87f75930200106&q=Kathmandu&days=1
 */
//@Query("access_key") key:String= API_KEY

interface ApiService {
    @GET("v1/current.json")
     fun getCurrentWeather(
        @Query("q") location: String,
    ): Deferred<CurrentWeatherResponse>

    @GET("v1/forecast.json")
     fun getFutureWeather(
        @Query("q") location: String,
        @Query("days") days: Int
    ): Deferred<FutureWeatherResponse>

    companion object {

        /**
         *  We can create operator with invoke as function name or any other name.
         *  Advantage of using invoke function is that,
         *  we can directly call <b>ApiService()</b> instead of ApiService.functionName()
         */
        operator fun invoke(connectivityInterceptor: ConnectivityInterceptor):ApiService {
            val requestInterceptor = Interceptor { chain ->
                val httpUrl =
                    chain.request().url()
                        .newBuilder().addQueryParameter("key", API_KEY)
                        .build()
                val request = chain.request().newBuilder().url(httpUrl).build()
                return@Interceptor chain.proceed(request)
            }
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()
             return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                 .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiService::class.java)
        }

    }
}