package com.example.weatherapp.adapters

import android.annotation.SuppressLint
import com.example.weatherapp.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.models.ForecastItem
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ForecastAdapter : ListAdapter<ForecastItem, ForecastAdapter.ForecastViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_forecast, parent, false)
        return ForecastViewHolder(view)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDate: TextView = itemView.findViewById(R.id.tvForecastDate)
        private val tvTemperature: TextView = itemView.findViewById(R.id.tvForecastTemperature)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvForecastDescription)
        private val ivIcon: ImageView = itemView.findViewById(R.id.ivForecastIcon)

        @SuppressLint("SetTextI18n")
        fun bind(forecastItem: ForecastItem) {
            val dateFormat = SimpleDateFormat("EEE, MMM dd, HH:mm", Locale.getDefault())
            tvDate.text = dateFormat.format(Date(forecastItem.date))
            tvTemperature.text = "${forecastItem.temperature.toInt()}°C"
            tvDescription.text = forecastItem.description.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }

            // Load weather icon
            val iconUrl = "https://openweathermap.org/img/wn/${forecastItem.iconCode}@2x.png"
            Picasso.get().load(iconUrl).into(ivIcon)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ForecastItem>() {
            override fun areItemsTheSame(oldItem: ForecastItem, newItem: ForecastItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ForecastItem, newItem: ForecastItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
