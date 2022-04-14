package com.example.citiesoftheworld.view.city

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.citiesoftheworld.R
import com.example.citiesoftheworld.database.model.cityAndCountry.CityAndCountry

import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

@ExperimentalStdlibApi
@ExperimentalCoroutinesApi
class CitiesAdapter : ListAdapter<CityAndCountry, RecyclerView.ViewHolder>(USER_COMPARATOR) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cityName: TextView = itemView.findViewById(R.id.cityName)
        private val cityLocalName: TextView = itemView.findViewById(R.id.cityLocalName)
        private val countryName: TextView = itemView.findViewById(R.id.countryName)

        @ExperimentalStdlibApi
        fun bind(cityAndCountry: CityAndCountry){

            cityName.text = cityAndCountry.city.name
            cityLocalName.text = cityAndCountry.city.localName
            countryName.text = cityAndCountry.country?.name

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.city_row_item, parent, false)
        return ViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cityAndCountry = getItem(position)
        (holder as ViewHolder).bind(cityAndCountry)
    }

    companion object {
        private val USER_COMPARATOR = object : DiffUtil.ItemCallback<CityAndCountry>() {
            override fun areItemsTheSame(oldItem: CityAndCountry, newItem: CityAndCountry):
                    Boolean = oldItem.city.id == newItem.city.id

            override fun areContentsTheSame(oldItem: CityAndCountry, newItem: CityAndCountry):
                    Boolean = oldItem == newItem
        }
    }

}
