package com.ayata.weatherappkotlin.ui.weather.future.list

import com.ayata.weatherappkotlin.R
import com.ayata.weatherappkotlin.data.unitlocalized.future.list.MetricSimpleFutureWeatherEntry
import com.ayata.weatherappkotlin.data.unitlocalized.future.list.UnitSpecificFutureWeatherEntry

import com.bumptech.glide.Glide
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_future_weather.*
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

class FutureWeatherItem(val weatherEntry: UnitSpecificFutureWeatherEntry) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            tv_description.text = "${weatherEntry.conditionText}"
            updateDate()
            updateIcon()
            updateTemperature()
        }
    }

    override fun getLayout() = R.layout.item_future_weather
    private fun ViewHolder.updateIcon() {
        Glide.with(containerView).load("https:${weatherEntry.conditionIconUrl}").into(iv_icon)
    }

    private fun ViewHolder.updateTemperature() {
        val unitAbbreviation = if (weatherEntry is MetricSimpleFutureWeatherEntry) "C" else "F"
        tv_temperature.text = "${weatherEntry.avgTemperature}$unitAbbreviation"
    }

    private fun ViewHolder.updateDate() {
        val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        tv_date.text = weatherEntry.date.format(dateFormatter)
    }


}