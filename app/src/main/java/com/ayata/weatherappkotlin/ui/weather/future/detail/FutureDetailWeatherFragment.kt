package com.ayata.weatherappkotlin.ui.weather.future.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ayata.weatherappkotlin.R
import com.ayata.weatherappkotlin.data.db.LocalDateConverter
import com.ayata.weatherappkotlin.internal.DateNotFoundException
import com.ayata.weatherappkotlin.ui.base.ScopedFragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.future_detail_weather_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory
import org.threeten.bp.LocalDate
class FutureDetailWeatherFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactoryInstanceFactory: ((LocalDate) -> FutureDetailWeatherViewModelFactory) by factory()

    private lateinit var viewModel: FutureDetailWeatherViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.future_detail_weather_fragment, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val safeArgs = arguments?.let { FutureDetailWeatherFragmentArgs.fromBundle(it) }
        val date =
            LocalDateConverter.stringToDate(safeArgs?.dateString) ?: throw DateNotFoundException()
        viewModel =
            ViewModelProviders.of(this, viewModelFactoryInstanceFactory(date))
                .get(FutureDetailWeatherViewModel::class.java)
        bindUIWeather()
        bindUI()
    }

    private fun bindUIWeather() = launch {
        val weatherLocation = viewModel.weatherLocation.await()
        weatherLocation.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            updateLocation(it.name)

        })
    }


    private fun bindUI() {

        viewModel.futureWeatherDetail.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            updateDateToClickedDate(it.date)
            updateTemperature(it.avgTemperature)
            updateMaxMinTemperature(it.mavTemperature, it.minTemperature)
            updatePrecipitation(it.totalPrecicpitation)
            updateImage(it.conditionIconUrl)
            updateWeatherDescription(it.conditionText)
            updateVisibility(it.avgVisibilityDistance)

        })
    }

    private fun updateTemperature(avgTemperature: Double) {
        val unit = chooseLocalizedUnitAbbreviation("°C", "F")
        textView_temperature.text = "$avgTemperature$unit"
    }

    private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String): String {
        return if (viewModel.isMetric) metric else imperial
    }

    private fun updateLocation(location: String) {
        (activity as AppCompatActivity).supportActionBar?.title = location
    }

    private fun updateMaxMinTemperature(maxTemp: Double, minTemp: Double) {
        val unit = chooseLocalizedUnitAbbreviation("°C", "F")
        textView_min_max_temperature.text = "Min: $minTemp$unit, Max: $maxTemp$unit"
    }

    private fun updateDateToClickedDate(date: LocalDate) {
        (activity as AppCompatActivity).supportActionBar?.subtitle = "${date}"
    }

    private fun updatePrecipitation(prep: Double) {
        val unit = chooseLocalizedUnitAbbreviation("mm", "in")
        textView_precipitation.text = "Total Precipitation: $prep$unit"
    }


    private fun updateVisibility(visibility: Double) {
        val unit = chooseLocalizedUnitAbbreviation("km", "miles")
        textView_visibility.text = "Visibility: $visibility$unit"
    }

    private fun updateWeatherDescription(des: String) {
        textView_condition.text = des
    }

    private fun updateImage(image: String) {
        Glide.with(requireContext()).load("https:$image").into(imageView_condition_icon)
    }

}