package com.ayata.weatherappkotlin.ui.weather.future.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayata.weatherappkotlin.R
import com.ayata.weatherappkotlin.data.db.LocalDateConverter
import com.ayata.weatherappkotlin.data.unitlocalized.future.list.UnitSpecificFutureWeatherEntry
import com.ayata.weatherappkotlin.ui.base.ScopedFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_future_list_weather.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import org.threeten.bp.LocalDate


class FutureListWeatherFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: FutureListWeatherViewModelFactory by instance()
    private lateinit var viewModel: FutureListWeatherViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.fragment_future_list_weather, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory)
                .get(FutureListWeatherViewModel::class.java)
        bindUI()

    }

    private fun bindUI() = launch(Dispatchers.Main) {
        val futureWeatherList = viewModel.futureWeatherEntries.await()
        val weatherLocation = viewModel.weatherLocation.await()
        weatherLocation.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            updateLocation(it.name)

        })
        futureWeatherList.observe(viewLifecycleOwner, Observer { futureList ->
            group_loading.visibility = View.GONE
            updateDateToNextWeek("Next Week")
            initRecyclerView(futureList.toFutureWeatherItem())
        })
    }

    private fun updateLocation(location: String) {
        (activity as AppCompatActivity).supportActionBar?.title = location
    }

    private fun updateDateToNextWeek(subtitle: String) {
        (activity as AppCompatActivity).supportActionBar?.subtitle = subtitle
    }

    private fun List<UnitSpecificFutureWeatherEntry>.toFutureWeatherItem(): List<FutureWeatherItem> {
        return this.map {
            FutureWeatherItem(it)
        }
    }

    private fun initRecyclerView(items: List<FutureWeatherItem>) {
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(items)
        }
        rv_futureListWeather.apply {
            layoutManager = LinearLayoutManager(this@FutureListWeatherFragment.context)
            adapter = groupAdapter
        }
        groupAdapter.setOnItemClickListener { item, view ->
            Toast.makeText(this.context, "Clicked", Toast.LENGTH_SHORT).show()
            (item as? FutureWeatherItem)?.let {
                showWeatherDetail(it.weatherEntry.date, view)
            }

        }
    }

    private fun showWeatherDetail(date: LocalDate, view: View) {
        val dateString = LocalDateConverter.dateToString(date)!!
        val action =
            FutureListWeatherFragmentDirections.actionFutureListWeatherFragmentToFutureDetailWeatherFragment(
                dateString
            )
        Navigation.findNavController(view).navigate(action)
    }
}