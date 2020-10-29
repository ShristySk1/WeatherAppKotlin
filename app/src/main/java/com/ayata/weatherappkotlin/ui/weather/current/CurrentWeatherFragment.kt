package com.ayata.weatherappkotlin.ui.weather.current

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ayata.weatherappkotlin.R
import com.ayata.weatherappkotlin.ui.base.ScopedFragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_current_weather.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CurrentWeatherFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()
    private lateinit var viewModel: CurrentWeatherViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.fragment_current_weather, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(CurrentWeatherViewModel::class.java)
        bindUI()

//
//        val apiService = ApiService(ConnectivityInterceptorImpl(requireContext()))
//        val weatherNetworkDatasource = WeatherNetworkDatasourceImpl(apiService)
//        weatherNetworkDatasource.downloadedCurrentWeather.observe(requireActivity(), {
//            textView.text = it.toString()
//        })
//        GlobalScope.launch(Dispatchers.Main) {
//            weatherNetworkDatasource.fetchCurrentWeather("Kathmandu")
//        }
    }

    private fun bindUI() = launch(Dispatchers.Main) {
        val currentWeather = viewModel.weather.await()
        val weatherLocation = viewModel.weatherLocation.await()
        weatherLocation.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            updateLocation(it.name)

        })
        currentWeather.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            group_loading.visibility=View.GONE
            updateDateToToday()
            updateTemperature(it.temperature, it.feelsLikeTemperature)
            updatePrecipitation(it.precipitationVolume)
            updateWindDirection(it.windDirection)
            updateImage(it.conditionIconUrl)
            updateWeatherDescription(it.conditionText)
            updateVisibility(it.visibilityDistance)

        })
    }

//            private fun bindUI() {
//        Log.d("fetchcome", "bindUI: ");
//        viewModel.currentWeatherData.observe(viewLifecycleOwner, Observer {
//            group_loading.visibility=View.GONE
//            if (it == null) {
//                Log.d("fetchinside", "bindUI: null");
//                return@Observer
//         }
//           tv_temp.text = it.toString()
//        })
//    }
    private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String): String {
        return if (viewModel.isMetric) metric else imperial
    }

    private fun updateLocation(location: String) {
        (activity as AppCompatActivity).supportActionBar?.title = location
    }

    private fun updateTemperature(temp: Double, feelsLike: Double) {
        val unit = chooseLocalizedUnitAbbreviation("C", "F")
        tv_temp.text = "$temp$unit"
        tv_feelsLike.text = "Feels like $feelsLike$unit"
    }

    private fun updateDateToToday() {
        (activity as AppCompatActivity).supportActionBar?.subtitle = "Today"
    }

    private fun updatePrecipitation(prep: Double) {
        val unit = chooseLocalizedUnitAbbreviation("mm", "in")
        tv_prep.text = "Precipitation: $prep$unit"
    }

    private fun updateWindDirection(direction: String) {

    }

    private fun updateVisibility(visibility: Double) {
        val unit = chooseLocalizedUnitAbbreviation("km", "miles")
        tv_visibility.text = "Visibility: $visibility$unit"
    }

    private fun updateWeatherDescription(des: String) {
        tv_desc.text = des
    }

    private fun updateImage(image: String) {
        Glide.with(requireContext()).load("https:$image").into(iv_weather)
    }
}