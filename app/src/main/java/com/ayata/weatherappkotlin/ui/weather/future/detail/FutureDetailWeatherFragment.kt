package com.ayata.weatherappkotlin.ui.weather.future.detail

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ayata.weatherappkotlin.R

class FutureDetailWeatherFragment : Fragment(R.layout.future_detail_weather_fragment) {

    companion object {
        fun newInstance() = FutureDetailWeatherFragment()
    }

    private lateinit var viewModel: FutureDetailWeatherViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FutureDetailWeatherViewModel::class.java)
        // TODO: Use the ViewModel
    }

}