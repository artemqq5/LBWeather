package com.lbweather.getweatherfromall.presentation.locationsFragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lbweather.getweatherfromall.R
import com.lbweather.getweatherfromall.databinding.ItemLocationBinding
import com.lbweather.getweatherfromall.data.database.LocationTable
import com.lbweather.getweatherfromall.presentation.locationsFragment.LocationAdapter.ViewHolder

class LocationAdapter(
    var dataSet: List<LocationTable>,
    private val listenerClick: NavigationInterfaceAdapter,
) :
    RecyclerView.Adapter<ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemLocationBinding.bind(view)

        @SuppressLint("SetTextI18n")
        fun bindItem(locationItem: LocationTable) {

            // set data to item
            binding.cityCountryInfo.text = locationItem.locationField
            binding.statusUse.visibility = locationItem.statusActive

            itemView.setOnClickListener {
                locationItem.statusActive = View.VISIBLE
                listenerClick.changeCurrentLocation(locationItem)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_location, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(dataSet[position])
    }

    override fun getItemCount(): Int = dataSet.size

    @SuppressLint("NotifyDataSetChanged")
    fun setList(newList: List<LocationTable>) {
        dataSet = newList
        notifyDataSetChanged()
    }

}